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

