package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    assertEquals(true, res.ok);
    assertEquals(14, res.consumedCount);
    assertEquals("another one", comment.text);
    tokenizer.increaseConsumedCount(res.consumedCount);

    comment = new MyComment();
    res = tokenizer.tokenize(comment);
    assertEquals(false, res.ok);
    assertEquals(0, res.consumedCount);
    assertEquals(null, comment.text);

    assertEquals("not a comment\n", tokenizer.getBuffer());

    // TODO: test needsMore by parsing comment with length > (buffer size, maxNeededStringSize)
  }

  @Test
  void tokenizeOuter() throws IOException, TokenizerException {

    InputStream stream = TokenizerTest.class.getResourceAsStream("example.custom-syntax");
    Tokenizer tokenizer = new Tokenizer(stream);

    MySyntax syntax = new MySyntax();
    TokenizeResult ok = tokenizer.tokenize(syntax);

    // TODO

  }
}

class MySyntax implements Token {
  public ArrayList<Token> comments = new ArrayList<>();

  public Token[] getInnerTokens() {
    return new Token[] {new MyComment(), new MyBlockComment()};
  }

  @Override
  public TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    comments.add(t);
    return res;
  }
}

class MyBlockComment extends MyComment {
  private static Pattern pattern = Pattern.compile("\\{\\s*([^\\}]*)(\\}?)", Pattern.MULTILINE);
}
