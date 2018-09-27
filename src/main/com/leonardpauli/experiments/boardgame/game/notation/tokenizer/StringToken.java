package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

public class StringToken implements Token {
  private String stringToMatch;

  public StringToken(String stringToMatch) {
    this.stringToMatch = stringToMatch;
  }

  @Override
  public TokenizeResult getMatchResult(String str) {
    if (str.length() < stringToMatch.length()) return new TokenizeResult(true);
    if (!str.substring(0, stringToMatch.length()).equals(stringToMatch))
      return new TokenizeResult();

    return new TokenizeResult(stringToMatch.length());
  }
}
