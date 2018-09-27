package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WrapperTokensTest {

  @Test
  void tokenizeOuter() throws IOException, TokenizerException {

    InputStream stream = TokenizerTest.class.getResourceAsStream("wrapper.custom-syntax");
    TestsSyntax ts = new TestsSyntax(stream);
    System.out.println(ts.sections.size());
  }
}

class MyTokenWrapperDemoSyntax implements Token {
  public Token[] getInnerTokens() {
    return new Token[] {new MyBlockComment(), new MyComment()};
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
    ArrayList<Test> tests = new ArrayList<>();

    @Override
    public TokenizeResult getMatchResult(Tokenizer tokenizer, int offset, String str)
        throws IOException, TokenizerException {
      int consumed = 0;
      TokenizeResult res;
      res = tokenizer.tokenize(title, offset + consumed);
      if (!res.ok) return res;
      consumed += res.consumedCount;

      while (true) {
        Test t = new Test();
        res = tokenizer.tokenize(t, offset + consumed);
        if (!res.ok) break;
        consumed += res.consumedCount;
        tests.add(t);
      }

      return new TokenizeResult(consumed);
    }

    static class Title implements Token {
      String value;

      static Pattern pattern = Pattern.compile("\\n*# ?([^\\n]*)\\n");

      public TokenizeResult getMatchResult(String str) {
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) return new TokenizeResult();
        value = matcher.group(1);
        return new TokenizeResult(matcher.end());
      }
    }

    static class Test implements Token {
      String value;
      boolean expectedOk;
      String expectedRest;

      boolean isExpectingAboutRest() {
        return expectedRest != null;
      }

      static Pattern pattern =
          Pattern.compile(
              "^(?!\\n|#)(?<value>[^\\s]+)( (?<expected>[^\\s]+)( (?<rest>rest [^\\n]+))?)?(\\n|$)");

      public TokenizeResult getMatchResult(String str) {
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) return new TokenizeResult();
        value = matcher.group("value");
        expectedOk =
            "true".equals(matcher.group("expected")); // TODO: error val for non boolean value
        expectedRest = matcher.group("rest");
        return new TokenizeResult(matcher.end());
      }
    }
  }
}
