package com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternToken implements Token {
  public Pattern getDefaultPattern() {
    return null;
  }

  private Pattern pattern;
  public Matcher matcher;
  public String patternName;

  public PatternToken(Pattern pattern) {
    this.pattern = pattern;
  }

  public PatternToken(Pattern pattern, String patternName) {
    this.pattern = pattern;
    this.patternName = patternName;
  }

  public PatternToken() {
    this.pattern = getDefaultPattern();
  }

  public void handleMatch() throws Exception {}

  @Override
  public TokenizeResult getMatchResult(String str) {
    matcher = pattern.matcher(str);
    if (!matcher.find()) return new TokenizeResult();
    try {
      handleMatch();
    } catch (Exception e) {
      return new TokenizeResult();
    }
    return new TokenizeResult(matcher.end());
  }
}
