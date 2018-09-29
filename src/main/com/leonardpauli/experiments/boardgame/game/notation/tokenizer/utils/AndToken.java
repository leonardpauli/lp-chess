package com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Tokenizer;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizerException;

import java.io.IOException;
import java.util.Arrays;

public class AndToken implements Token {

  private Token[] tokens;
  private Token[] matchedTokens;

  public AndToken(Token[] tokens) {
    this.tokens = tokens;
  }

  public Token[] getTokens() {
    return matchedTokens;
  }

  public TokenizeResult getMatchResult(Tokenizer tokenizer, int offset, String str)
      throws IOException, TokenizerException {
    matchedTokens = new Token[tokens.length];
    int i = 0;
    int consumed = 0;
    for (Token t : tokens) {
      TokenizeResult res = tokenizer.tokenize(t, offset + consumed);
      if (!res.ok && !(t instanceof OptionalToken)) return res;
      if (res.ok) matchedTokens[i++] = t;
      consumed += res.consumedCount;
    }
    matchedTokens = Arrays.copyOf(matchedTokens, i);
    return new TokenizeResult(consumed);
  }
}
