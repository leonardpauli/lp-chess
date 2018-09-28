package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OptionalToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.Whitespace;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comment implements Token {
  public String getText() {
    return comment.text;
  };

  public LineComment comment;

  public Token[] getInnerTokens() {
    return new Token[] {new BlockComment(), new LineComment()};
  }

  @Override
  public TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    comment = (LineComment) t;
    return res;
  }

  public static class LineComment implements Token {
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

  public static class BlockComment extends LineComment {
    private static Pattern pattern = Pattern.compile("^\\{\\s*((?:\\s*[^\\}\\s]+)*)(\\s*\\}?)");

    @Override
    public TokenizeResult getMatchResult(String str) {
      return getMatchResult(str, pattern);
    }
  }

  public static class Section implements Token {
    Comment comment;

    public Token[] getInnerTokens() {
      return new Token[] {
        new AndToken(
            new Token[] {
              new OptionalToken(new Whitespace()),
              new OptionalToken(new Comment()),
              new OptionalToken(new Whitespace())
            })
      };
    }

    @Override
    public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
      AndToken a = (AndToken) at;
      for (Token ot : a.getTokens()) {
        Token t = ((OptionalToken) ot).getToken();
        if (t instanceof Comment) comment = (Comment) t;
      }
      return res;
    }
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
