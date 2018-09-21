package com.leonardpauli.experiments.boardgame.game.notation;

public interface Token {
  default String getTokenStartRegex() {
    return "^";
  }

  default Token[] getInnerTokens() {
    return new Token[] {};
  }

  default TokenizeResult handleMatch(String str) {
    return new TokenizeResult();
  }

  default TokenizeResult handleInnerMatch(Token t, String str) {
    return new TokenizeResult();
  }
}

class TokenizeResult {
  public final boolean ok;
  public final int consumedCount;

  public TokenizeResult() {
    this.ok = false;
    this.consumedCount = 0;
  }

  public TokenizeResult(int consumedCount) {
    this.ok = true;
    this.consumedCount = consumedCount;
  }
}
