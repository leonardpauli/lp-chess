package com.leonardpauli.experiments.boardgame.board.tile;

import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.util.Point;

public class Position extends Point {

  public Position(int x, int y) {
    super(x, y);
  }

  public Position(Point p) {
    super(p.x, p.y);
  }

  public static Position fromString(String code) throws GameException {
    if (code.length() != 2)
      throw new GameException("code.length has to be 2, was " + code.length());
    int x = ((int) code.toUpperCase().charAt(0)) - 65;
    if (x < 0 || x > ((int) 'z') - 65) throw new GameException("code.x not in range, was " + x);
    int y = Integer.parseInt(code.substring(1, 2)) - 1;
    return new Position(x, y);
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
}
