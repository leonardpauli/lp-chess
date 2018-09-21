package com.leonardpauli.experiments.boardgame.game.notation;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class TokenizerTest {

  @Test
  void tokenize() throws IOException {

    InputStream stream = TokenizerTest.class.getResourceAsStream("carlsen-excerpt.pgn");
    Tokenizer tokenizer = new Tokenizer(stream);

    MyComment comment = new MyComment();
    TokenizeResult ok = tokenizer.tokenize(comment);

    System.out.println("was ok: " + ok);
    System.out.println("comment: " + comment);

    MySyntax syntax = new MySyntax();
    TokenizeResult ok2 = tokenizer.tokenize(syntax);

    System.out.println("was ok: " + ok2);
    System.out.println("syntax: " + syntax);
  }
}

class MySyntax implements Token {
  public Token[] getInnerTokens() {
    return new Token[] {new MyComment(), new MyBlockComment()};
  }

  @Override
  public TokenizeResult handleMatch() {
    return new TokenizeResult(1);
  }
}

class MyComment implements Token {
  public String getTokenStartRegex() {
    return ";";
  }
}

class MyBlockComment implements Token {
  public String getTokenStartRegex() {
    return "{";
  }
}
