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
            new RepeatToken(() -> new OrToken(new Token[] {new Move(), new StatusToken()}))
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
      } else if (ai instanceof RepeatToken) {
        for (Token rt : ((RepeatToken) ai).getTokens()) {
          Token ot = ((OrToken) rt).getMatchedToken();
          if (ot instanceof StatusToken) status = (StatusToken) ot;
          else if (ot instanceof Move) {
            Move[] m2 = new Move[(moves == null ? 0 : moves.length) + 1];
            if (moves != null) System.arraycopy(moves, 0, m2, 0, moves.length);
            m2[moves == null ? 0 : moves.length] = ((Move) ot);
            moves = m2;
          }
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

    public static class GameOver extends PatternToken {
      private static Pattern p = Pattern.compile("^(?<white>0|1|1/2|½)-(?<black>0|1|1/2|½)");

      enum Score {
        LOST("0"),
        DRAW("1/2", "½"),
        WON("1");

        public final String code;
        public final String alt;

        Score(String code, String alt) {
          this.code = code;
          this.alt = alt;
        }

        Score(String code) {
          this.code = code;
          this.alt = null;
        }

        public static Score fromCodeUnsafe(String code) {
          for (Score t : Score.values()) {
            if (code.equals(t.code) || code.equals(t.alt)) return t;
          }
          return null;
        }
      }

      public Score[] scores = new Score[] {Score.DRAW, Score.DRAW};

      @Override
      public void handleMatch() throws Exception {
        scores[0] = Score.fromCodeUnsafe(matcher.group("white"));
        scores[1] = Score.fromCodeUnsafe(matcher.group("black"));
      }
    }
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
