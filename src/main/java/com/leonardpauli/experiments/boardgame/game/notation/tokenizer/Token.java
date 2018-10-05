package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import java.io.IOException;

public interface Token {
  default Token[] getInnerTokens() {
    return new Token[] {};
  }

  // static Pattern pattern = Pattern.compile("# ?(.*)\\n");

  default TokenizeResult getMatchResult(String str) {
    /*
    Matcher matcher = pattern.matcher(str);
    if (!matcher.find()) return new TokenizeResult();
    value = matcher.group(1);
    return new TokenizeResult(matcher.end());
    */
    return new TokenizeResult();
  }

  default TokenizeResult getMatchResult(Tokenizer tokenizer, int offset, String str)
      throws IOException, TokenizerException {
    return getMatchResult(str);
  }

  default TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    return res;
  }
}
