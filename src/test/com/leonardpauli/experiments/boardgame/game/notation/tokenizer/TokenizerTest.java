package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import com.leonardpauli.experiments.boardgame.util.Util;
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
    // System.out.println(res);
    // System.out.println(Util.objectToString(comment));
    assertEquals(true, res.ok);
    assertEquals(12, res.consumedCount);
    assertEquals("a comment", comment.text);

    MyComment comment2 = new MyComment();
    TokenizeResult res2 = tokenizer.tokenize(comment);
    System.out.println(Util.objectToString(comment2));
    assertEquals(true, res2.ok);
    assertEquals(14, res2.consumedCount);
    assertEquals("another one", comment2.text);

    MyComment comment3 = new MyComment();
    TokenizeResult res3 = tokenizer.tokenize(comment);
    assertEquals(false, res3.ok);
    assertEquals(0, res2.consumedCount);
    assertEquals(null, comment3.text);

    // TODO: test needsMore by parsing comment with length > (buffer size, maxNeededStringSize)
  }

  @Test
  void tokenizeOuter() throws IOException, TokenizerException {

    InputStream stream = TokenizerTest.class.getResourceAsStream("example.custom-syntax");
    Tokenizer tokenizer = new Tokenizer(stream);

    MySyntax syntax = new MySyntax();
    TokenizeResult ok = tokenizer.tokenize(syntax);
    System.out.println("was ok: " + ok);
    System.out.println("syntax: " + syntax);
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
