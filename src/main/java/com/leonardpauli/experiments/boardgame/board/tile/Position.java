package com.leonardpauli.experiments.boardgame.board.tile;

import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.util.Point;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Position extends Point {

  public Position(int x, int y) {
    super(x, y);
  }

  public Position(Point p) {
    super(p.x, p.y);
  }

  public static Position fromString(String code) throws GameException {
    Optional p = Optional.fromString(code);
    if (!p.hasX()) throw new GameException("no x provided");
    if (!p.hasY()) throw new GameException("no y provided");
    return p.getPosition(0, 0);
  }

  public String toString() {
    return colString() + rowString();
  }

  public String colString() {
    return Character.toString((char) (x + 65));
  }

  public String rowString() {
    return Integer.toString(y + 1);
  }

  public static class Optional implements Token {
    public OptionalInt x;
    public OptionalInt y;

    private boolean matchEmpty = true;

    public Optional(OptionalInt x, OptionalInt y) {
      this.x = x;
      this.y = y;
    }

    public Optional() {
      this.x = OptionalInt.empty();
      this.y = OptionalInt.empty();
    }

    public Optional(boolean matchEmpty) {
      this.matchEmpty = matchEmpty;
    }

    public boolean hasX() {
      return x.isPresent();
    }

    public boolean hasY() {
      return y.isPresent();
    }

    public boolean isEmpty() {
      return !hasX() && !hasY();
    }

    public int getX(int defaultValue) {
      return x.orElse(defaultValue);
    }

    public int getY(int defaultValue) {
      return y.orElse(defaultValue);
    }

    public void setX(int v) {
      x = OptionalInt.of(v);
    }

    public void setY(int v) {
      y = OptionalInt.of(v);
    }

    public void emptyX() {
      x = OptionalInt.empty();
    }

    public void emptyY() {
      y = OptionalInt.empty();
    }

    public void empty() {
      emptyY();
      emptyX();
    }

    public Position getPosition(int defaultX, int defaultY) {
      return new Position(getX(defaultX), getY(defaultY));
    }

    static Pattern pattern = Pattern.compile("^([a-w]?)(\\d*(?!\\d|\\.))");

    public void setFrom(Position.Optional op) {
      this.x = op.x;
      this.y = op.y;
    }

    public Matcher setFrom(String code) {
      Matcher m = pattern.matcher(code);
      if (!m.find()) {
        empty();
        return m;
      }

      if (m.group(1).length() > 0) {
        int x = 0;
        for (char c : m.group(1).toUpperCase().toCharArray()) x += ((int) c) - 65;
        setX(x);
      } else emptyX();

      if (m.group(2).length() > 0) setY(Integer.parseInt(m.group(2)) - 1);
      else emptyY();

      return m;
    }

    public static Position.Optional fromString(String code) {
      Optional p = new Optional();
      p.setFrom(code);
      return p;
    }

    @Override
    public TokenizeResult getMatchResult(String str) {
      Matcher m = setFrom(str);
      if (!matchEmpty && isEmpty()) return new TokenizeResult();
      return new TokenizeResult(m.end());
    }

    @Override
    public String toString() {
      return Util.objectToString(this);
    }
  }
}
