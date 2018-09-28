package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

public class OrToken implements Token {

  public OrToken(Token[] tokens) {
    this.tokens = tokens;
  }

  private Token[] tokens;

  @Override
  public Token[] getInnerTokens() {
    return tokens;
  }

  private Token matchedToken;

  public Token getMatchedToken() {
    return matchedToken;
  }

  @Override
  public TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    matchedToken = t;
    return res;
  }
}
