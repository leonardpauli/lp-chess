package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.State;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.*;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Round implements Token {
  public Ordinal ordinal;
  public Move[] moves;
  public StatusToken status;

  @Override
  public Token[] getInnerTokens() {
    return new Token[] {
      new AndToken(
          new Token[] {
            new OptionalToken(new Ordinal()),
            new OptionalToken(new Whitespace()),
            new OrToken(new Token[] {new StatusToken(), new RepeatToken(() -> new Move())})
          })
    };
  }

  @Override
  public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
    AndToken a = (AndToken) at;
    for (Token ai : a.getTokens()) {
      if (ai instanceof OptionalToken) {
        Token ot = ((OptionalToken) ai).getToken();
        if (ot instanceof Ordinal) ordinal = (Ordinal) ot;
      } else if (ai instanceof OrToken) {
        Token oi = ((OrToken) ai).getMatchedToken();
        if (oi instanceof StatusToken) status = (StatusToken) oi;
        else if (oi instanceof RepeatToken) {
          moves = ((RepeatToken) oi).getTokens().toArray(new Move[] {});
        }
      }
    }
    return res;
  }

  public static class Ordinal implements Token {
    public int nr;

    private static Pattern pattern = Pattern.compile("^(?<nr>\\d+)\\.");

    @Override
    public TokenizeResult getMatchResult(String str) {
      Matcher m = pattern.matcher(str);
      if (!m.find()) return new TokenizeResult();
      nr = Integer.valueOf(m.group("nr"));
      return new TokenizeResult(m.end());
    }

    @Override
    public String toString() {
      return Util.objectToString(this);
    }
  }

  static class StatusToken implements Token {
    State state;
    String text;
    private boolean matingPlayerIsWhite = true;

    // TODO: followed by Comment.Part

    private static Pattern pattern =
        Pattern.compile("^((?<draw>1/2-1/2)|(?<black>0-1)|(?<white>1-0))");

    @Override
    public TokenizeResult getMatchResult(String str) {
      Matcher m = pattern.matcher(str);
      if (!m.find()) return new TokenizeResult();
      state =
          m.group("draw") != null
              ? State.DRAW
              : m.group("black") != null ? State.CHECKMATE : State.CHECKMATE;
      matingPlayerIsWhite = m.group("white") != null;
      text = m.group();
      return new TokenizeResult(m.end());
    }

    public boolean didWhiteCauseMate() {
      return matingPlayerIsWhite && (state == State.CHECKMATE);
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
