package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class WrapperTokensTest {

  static TestsSyntax syntax;

  @BeforeAll
  static void parseTests() throws IOException, TokenizerException {
    if (syntax != null) return;
    InputStream stream = TokenizerTest.class.getResourceAsStream("wrapper.custom-syntax");
    syntax = new TestsSyntax(stream);
  }

  @Test
  void wrapperTestsParsed() {
    assertEquals(6, syntax.sections.size());
  }

  @TestFactory
  Stream<DynamicTest> wrapperTests() {
    return syntax
        .sections
        .stream()
        .filter(s -> !s.title.skip)
        // .filter(s -> s.title.value.equals("ab")) // for debugging one
        .map(TestsSyntax.Section::getTests)
        .reduce(Stream::concat)
        .get();
  }
}

class TestsSyntax implements Token {
  // bc context free, could be solved with regex alone:
  // (given that you can repeat named match groups with siblings)
  // (:<section># (:<title>.*)\n(:<test>(:<value>[^ ]+)(:<expected> [^ ]+(:<rest> .+)?)?\n)*\n?)*

  ArrayList<Section> sections = new ArrayList<>();

  TestsSyntax(InputStream stream) throws IOException, TokenizerException {
    Tokenizer tokenizer = new Tokenizer(stream);
    TokenizeResult res;
    while ((res = tokenizer.tokenize(this)).ok) {
      tokenizer.increaseConsumedCount(res.consumedCount);
    }
  }

  public Token[] getInnerTokens() {
    return new Token[] {new Section()};
  }

  @Override
  public TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    sections.add((Section) t);
    return res;
  }

  static class Section implements Token {

    Title title = new Title();
    ArrayList<TestSpecLine> testSpecLines = new ArrayList<>();

    @Override
    public TokenizeResult getMatchResult(Tokenizer tokenizer, int offset, String str)
        throws IOException, TokenizerException {
      int consumed = 0;
      TokenizeResult res;
      res = tokenizer.tokenize(title, offset + consumed);
      if (!res.ok) return res;
      consumed += res.consumedCount;

      while (true) {
        TestSpecLine t = new TestSpecLine();
        res = tokenizer.tokenize(t, offset + consumed);
        if (!res.ok) break;
        consumed += res.consumedCount;
        testSpecLines.add(t);
      }

      return new TokenizeResult(consumed);
    }

    static void testOne(TestSpecLine t, Title title) {
      InputStream inp = new ByteArrayInputStream(t.value.getBytes());
      Tokenizer tnr = new Tokenizer(inp);
      TestRunnerToken token = new TestRunnerToken(title::getTokens);

      TokenizeResult res = null;
      try {
        res = tnr.tokenize(token);
      } catch (Exception e) {
        assertEquals(null, e);
      }
      tnr.increaseConsumedCount(res.consumedCount);

      assertEquals(t.expectedOk, res.ok, "ok");
      if (t.isExpectingAboutRest()) assertEquals(t.expectedRest, tnr.getRest(), "rest");
    }

    Stream<DynamicTest> getTests() {
      return testSpecLines
          .stream()
          .map(
              t ->
                  dynamicTest(
                      title.value + ": " + t.value + (t.expectedOk ? "" : " (false)"),
                      () -> testOne(t, title)));
    }

    static class TestRunnerToken implements Token {

      Token resToken;

      interface InnerTokensGetter {
        Token[] getInnerTokens();
      }

      private InnerTokensGetter tokensGetter;

      TestRunnerToken(InnerTokensGetter tokensGetter) {
        this.tokensGetter = tokensGetter;
      }

      @Override
      public Token[] getInnerTokens() {
        return tokensGetter.getInnerTokens();
      }

      @Override
      public TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
        resToken = t;
        return res;
      }
    }

    static class Title implements Token {

      String value;
      boolean skip;

      static Pattern pattern = Pattern.compile("\\n*# ?(?<skip>skip )?(?<value>[^\\n]*)\\n");

      public TokenizeResult getMatchResult(String str) {
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) return new TokenizeResult();
        value = matcher.group("value");
        skip = matcher.group("skip") != null;
        return new TokenizeResult(matcher.end());
      }

      Token[] getTokens() {
        ArrayList<Token> orTokens = new ArrayList<>();
        for (String s : value.split("\\|")) {
          ArrayList<Token> andTokens = new ArrayList<>();
          for (int i = 0; i < s.length(); i += 2) {
            String c = s.substring(i, i + 1);
            String n = i + 1 < s.length() ? s.substring(i + 1, i + 2) : "";
            if (n.equals("*")) {
              andTokens.add(new OptionalToken(new RepeatToken(() -> new StringToken(c))));
            } else if (n.equals("+")) {
              andTokens.add((new RepeatToken(() -> new StringToken(c))));
            } else if (n.equals("?")) {
              andTokens.add(new OptionalToken((new StringToken(c))));
            } else {
              andTokens.add(new StringToken(c));
              i--;
            }
          }
          orTokens.add(new AndToken(andTokens.toArray(new Token[] {})));
        }
        return orTokens.toArray(new Token[] {});
      }
    }

    static class TestSpecLine implements Token {
      String value;
      boolean expectedOk;
      String expectedRest;

      boolean isExpectingAboutRest() {
        return expectedRest != null;
      }

      static Pattern pattern =
          Pattern.compile(
              "^(?!\\n|#)(?<value>[^\\s]+)( (?<expected>[^\\s]+)( rest (?<rest>[^\\n]+))?)?(\\n|$)");

      public TokenizeResult getMatchResult(String str) {
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) return new TokenizeResult();
        value = matcher.group("value");
        expectedOk =
            "true".equals(matcher.group("expected")); // TODO: error val for non boolean value
        expectedRest = matcher.group("rest");

        expectedRest =
            expectedRest == null && expectedOk
                ? ""
                : expectedRest; // always check rest for now if ok

        return new TokenizeResult(matcher.end());
      }
    }
  }
}
