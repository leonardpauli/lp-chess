package com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Tokenizer;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizerException;

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
