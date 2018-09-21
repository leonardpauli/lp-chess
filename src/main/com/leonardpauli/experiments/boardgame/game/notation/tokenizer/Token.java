package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

public interface Token {
  default Token[] getInnerTokens() {
    return new Token[] {};
  }

  default TokenizeResult getMatchResult(String str) {
    /*
    Pattern pattern = Pattern.compile("a", Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(str);
    int i = matcher.find();
    this.x = str.substring(0, i);
    return new TokenizeResult(i);
    */
    return new TokenizeResult();
  }

  default TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    return res;
  }
}

class TokenizeResult {
  public final boolean ok;
  public final boolean needsMore;
  public final int consumedCount;
  public int maxNeededStringSize = 32768;

  public TokenizeResult() {
    this.ok = false;
    this.needsMore = false;
    this.consumedCount = 0;
  }

  public TokenizeResult(boolean needsMore) {
    this.ok = false;
    this.needsMore = needsMore;
    this.consumedCount = 0;
  }

  public TokenizeResult(int consumedCount) {
    this.ok = true;
    this.needsMore = false;
    this.consumedCount = consumedCount;
  }
}
