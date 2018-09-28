package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OptionalToken;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tag implements Token {
  public String key;
  public String value;

  static Pattern pattern =
      Pattern.compile("^\\[(?<key>[^\\s\\]]+)( \"(?<value>([^\"\\\\]|\\\\[\\\\\"])*)\")?\\]");

  @Override
  public TokenizeResult getMatchResult(String str) {
    Matcher m = pattern.matcher(str);
    if (!m.find()) return new TokenizeResult();
    key = m.group("key");
    value = m.group("value");

    // TODO: if (hasValue()) clean up escaped chars in value

    return new TokenizeResult(m.end());
  }

  public boolean hasValue() {
    return value != null;
  }

  public static class Section implements Token {
    public Tag tag;
    public Comment comment;

    public Token[] getInnerTokens() {
      return new Token[] {
        new AndToken(
            new Token[] {
              new Tag(), new OptionalToken(new Comment.Section()),
            })
      };
    }

    @Override
    public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
      AndToken a = (AndToken) at;
      for (Token t : a.getTokens()) {
        if (t instanceof Tag) tag = (Tag) t;
        else if (t instanceof OptionalToken) {
          Comment.Section ct = (Comment.Section) ((OptionalToken) t).getToken();
          comment = ct.comment;
        }
      }
      return res;
    }

    @Override
    public String toString() {
      return Util.objectToString(this);
    }
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
