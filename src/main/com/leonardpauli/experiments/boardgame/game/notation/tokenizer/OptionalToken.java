package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import java.io.IOException;

public class OptionalToken implements Token {

  private Token token;

  public OptionalToken(Token token) {
    this.token = token;
  }

  public Token getToken() {
    return token;
  }

  public TokenizeResult getMatchResult(Tokenizer tokenizer, int offset, String str)
      throws IOException, TokenizerException {
    TokenizeResult res = tokenizer.tokenize(token, offset);
    return res;
  }
}
