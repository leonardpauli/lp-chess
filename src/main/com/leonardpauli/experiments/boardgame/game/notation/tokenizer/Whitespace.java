package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Whitespace implements Token {
  public String text;
  private static Pattern pattern = Pattern.compile("^[\\s\\n\\r]+");

  @Override
  public TokenizeResult getMatchResult(String str) {
    Matcher matcher = pattern.matcher(str);
    boolean found = matcher.find();
    if (!found) return new TokenizeResult();

    text = str.substring(matcher.start(), matcher.end());
    return new TokenizeResult(matcher.end());
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
