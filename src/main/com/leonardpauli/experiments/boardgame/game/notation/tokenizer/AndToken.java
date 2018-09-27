package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import java.io.IOException;

public class AndToken implements Token {

  private Token[] tokens;

  public AndToken(Token[] tokens) {
    this.tokens = tokens;
  }

  public Token[] getTokens() {
    return tokens;
  }

  public TokenizeResult getMatchResult(Tokenizer tokenizer, int offset, String str)
      throws IOException, TokenizerException {
    int consumed = 0;
    for (Token t : tokens) {
      TokenizeResult res = tokenizer.tokenize(t, offset + consumed);
      if (!res.ok && !(t instanceof OptionalToken)) return res;
      consumed += res.consumedCount;
    }
    return new TokenizeResult(consumed);
  }
}
