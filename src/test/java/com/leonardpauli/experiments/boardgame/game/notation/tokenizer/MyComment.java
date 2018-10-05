package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyComment implements Token {
  public String text;

  private static Pattern pattern = Pattern.compile("^;[ \\t]*([^\\n]*)(\n?)");

  @Override
  public TokenizeResult getMatchResult(String str) {
    return getMatchResult(str, pattern);
  }

  public TokenizeResult getMatchResult(String str, Pattern pattern) {
    Matcher matcher = pattern.matcher(str);
    boolean found = matcher.find();
    if (!found) return new TokenizeResult();
    boolean ended = matcher.end(2) != matcher.start(2);
    if (!ended) return new TokenizeResult(true);

    text = str.substring(matcher.start(1), matcher.end(1));
    return new TokenizeResult(matcher.end());
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
