package com.leonardpauli.experiments.boardgame.game;

public enum State {
  // descriptions originally from wikipedia
  DEFAULT("Default", ""),
  CHECK("Check", "escapable immediate threat for target's king by source"),
  DRAW("Draw", ""),
  STALEMATE("Stalemate", "player unable to move, but isn't in check or checkmate"),
  CHECKMATE("Checkmate", "");

  String title;
  String description;

  State(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public boolean isMate() {
    return this == STALEMATE || this == CHECKMATE;
  }
}
