package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.OptionalToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tag implements Token {
  public String key;
  public String value;

  static Pattern pattern =
      Pattern.compile("^\\[(?<key>[^\\s]+)? ?(\"(?<value>[^\"\\\\]|\\\\[\\\\\"])\")\\]");

  @Override
  public TokenizeResult getMatchResult(String str) {
    Matcher m = pattern.matcher(str);
    if (!m.find()) return new TokenizeResult();
    key = m.group("key");
    value = m.group("value");
    return new TokenizeResult(m.end());
  }

  public static class Section implements Token {
    Tag tag;
    Comment comment;

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
  }
}
