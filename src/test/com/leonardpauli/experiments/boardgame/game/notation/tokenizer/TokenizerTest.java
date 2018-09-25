package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

  @Test
  void tokenizeInner() throws IOException, TokenizerException {

    InputStream stream = TokenizerTest.class.getResourceAsStream("example.custom-syntax");
    Tokenizer tokenizer = new Tokenizer(stream);

    MyComment comment = new MyComment();
    TokenizeResult res = tokenizer.tokenize(comment);
    assertEquals(true, res.ok);
    assertEquals(12, res.consumedCount);
    assertEquals("a comment", comment.text);
    tokenizer.increaseConsumedCount(res.consumedCount);

    comment = new MyComment();
    res = tokenizer.tokenize(comment);
    assertTrue(res.ok);
    assertEquals(14, res.consumedCount);
    assertEquals("another one", comment.text);
    tokenizer.increaseConsumedCount(res.consumedCount);

    comment = new MyBlockComment();
    res = tokenizer.tokenize(comment);
    assertTrue(res.ok);
    assertEquals(17, res.consumedCount);
    assertEquals("block comment", comment.text);
    tokenizer.increaseConsumedCount(res.consumedCount);

    comment = new MyComment();
    res = tokenizer.tokenize(comment);
    assertFalse(res.ok);
    assertEquals(0, res.consumedCount);
    assertNull(comment.text);

    assertEquals("\nnot a comment\n", tokenizer.getBuffer());

    // TODO: test needsMore by parsing comment with length > (buffer size, maxNeededStringSize)
  }

  @Test
  void tokenizeOuter() throws IOException, TokenizerException {

    InputStream stream = TokenizerTest.class.getResourceAsStream("example.custom-syntax");
    Tokenizer tokenizer = new Tokenizer(stream);

    MySyntax syntax = new MySyntax();
    TokenizeResult res = null;
    while ((res = tokenizer.tokenize(syntax)).ok) {
      tokenizer.increaseConsumedCount(res.consumedCount);
    }

    StringBuilder sb = new StringBuilder();
    for (Token c : syntax.comments) sb.append(c.toString() + "\n");
    assertEquals(
        "MyComment{text: a comment}\n"
            + "MyComment{text: another one}\n"
            + "MyBlockComment{text: block comment}\n",
        sb.toString());
  }
}

class MySyntax implements Token {
  public ArrayList<Token> comments = new ArrayList<>();

  public Token[] getInnerTokens() {
    return new Token[] {new MyBlockComment(), new MyComment()};
  }

  @Override
  public TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    comments.add(t);
    return res;
  }
}

class MyBlockComment extends MyComment {
  private static Pattern pattern = Pattern.compile("^\\{\\s*((?:\\s*[^\\}\\s]+)*)(\\s*\\}?)");

  @Override
  public TokenizeResult getMatchResult(String str) {
    return getMatchResult(str, pattern);
  }
}
