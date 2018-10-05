package com.leonardpauli.experiments.boardgame.board.movement;

public enum MovementType {
  ONE_STEP("one-step"),

  DIAGONAL("diagonal"),
  STRAIGHT("straight"),

  FORWARD_ONE("forward-one"),
  FORWARD_TWO_FROM_HOME("forward-two-from-home"),
  FORWARD_DIAGONAL_ONE_CAPTURE("forward-diagonal-one"),

  LMOVE("l-move"), // eg. the horse
  CASTLING("castling"), // tower and king move
  ENPASSANT("en-passant"), // diagonal capturing move by pawn
  PROMOTION("promotion"); // pawn converting to other piece at other players home rank

  public final String name;

  MovementType(String name) {
    this.name = name;
  }
}
