package com.leonardpauli.experiments.boardgame.board.tile;

import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.util.Point;

public enum EdgeType {
  LEFT('<', new int[] {-1, 0}, "left"),
  RIGHT('>', new int[] {1, 0}, "right"),
  UP('^', new int[] {0, 1}, "up"),
  DOWN('v', new int[] {0, -1}, "down"),
  ANY('.', new int[] {0, 0}, "any");

  public final char code;
  public final Point direction;
  public final String title;
  private EdgeType turned;

  EdgeType(char code, int[] dir, String title) {
    this.code = code;
    this.direction = new Point(dir[0], dir[1]);
    this.title = title;
  }

  // turning

  static {
    LEFT.turned = DOWN;
    DOWN.turned = RIGHT;
    RIGHT.turned = UP;
    UP.turned = LEFT;
  }

  public boolean canBeTurned() {
    return turned != null;
  }

  public EdgeType getTurned(int times) throws GameException {
    EdgeType type = this;
    for (int i = 0; i < times; i++) {
      if (!type.canBeTurned()) throw new GameException("edge can't be turned");
      type = type.turned;
    }
    return type;
  }

  public EdgeType getTurned() throws GameException {
    return getTurned(1);
  }

  public int getTurns(EdgeType toType, int limit) throws GameException {
    if (limit == 0) throw new GameException("no turns found within limit");
    return toType == this ? 0 : 1 + this.getTurned().getTurns(toType, limit - 1);
  }

  public int getTurns(EdgeType toType) throws GameException {
    return toType == this ? 0 : 1 + this.getTurned().getTurns(toType, 10);
  }

  public static EdgeType[] turnedPath(EdgeType[] path, int turns) throws GameException {
    EdgeType[] result = new EdgeType[path.length];
    for (int i = 0; i < path.length; i++) result[i] = path[i].getTurned(turns);
    return result;
  }

  // string

  public static EdgeType fromCode(char code) throws GameException {
    for (EdgeType t : EdgeType.values()) if (t.code == code) return t;
    throw new GameException("enum not found");
  }

  public static EdgeType[] getPath(String code) throws GameException {
    EdgeType[] path = new EdgeType[code.length()];
    int i = 0;
    for (char ch : code.toCharArray()) path[i++] = fromCode(ch);
    return path;
  }

  public static String stringFromPath(EdgeType[] path) {
    StringBuilder sb = new StringBuilder();
    for (EdgeType type : path) sb.append(String.valueOf(type.code));
    return sb.toString();
  }
}
