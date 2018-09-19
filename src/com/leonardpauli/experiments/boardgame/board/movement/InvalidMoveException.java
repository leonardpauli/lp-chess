package com.leonardpauli.experiments.boardgame.board.movement;

import com.leonardpauli.experiments.boardgame.game.GameException;

public class InvalidMoveException extends GameException {

  public enum Type {
    DESTINATION_OCCUPIED("destination tile occupied"),
    DESTINATION_NOT_FOUND("destination tile not found"),
    OTHER("other");

    private final String description;

    Type(String description) {
      this.description = description;
    }

    public String toString() {
      return description;
    }
  }

  public Type type;

  public InvalidMoveException(Type type) {
    super("Invalid move; " + type.toString());
    this.type = type;
  }

  public InvalidMoveException(String msg) {
    super("Invalid move; " + msg);
    this.type = Type.OTHER;
  }
}
