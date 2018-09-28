package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import java.io.IOException;
import java.util.ArrayList;

public class RepeatToken implements Token {
  private ArrayList<Token> tokens = new ArrayList<>();
  private TokenGetter tokenGetter;

  public RepeatToken(TokenGetter tokenGetter) {
    this.tokenGetter = tokenGetter;
  }

  public ArrayList<Token> getTokens() {
    return tokens;
  }

  public TokenizeResult getMatchResult(Tokenizer tokenizer, int offset, String str)
      throws IOException, TokenizerException {
    int consumed = 0;
    TokenizeResult res;
    Token t;
    do {
      t = tokenGetter.getNewToken();
      res = tokenizer.tokenize(t, offset + consumed);
      if (res.ok) {
        if (res.consumedCount == 0)
          throw new TokenizerException(
              "in RepeatToken, got 0 consumed but ok=true; would lead to infinite loop");
        tokens.add(t);
        consumed += res.consumedCount;
      }
    } while (res.ok);
    if (tokens.size() == 0) return new TokenizeResult();
    return new TokenizeResult(consumed);
  }
}
