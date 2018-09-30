package com.leonardpauli.experiments.boardgame.board.movement;

import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;

import java.util.Optional;

public class Movement {
  public MovementType type;
  public Edge edge;
  private Optional<Piece> capturedPiece = Optional.empty();

  public Movement(MovementType type, Edge edge) {
    this.type = type;
    this.edge = edge;
  }

  public void setCapturedPiece(Piece piece) {
    capturedPiece = Optional.of(piece);
  }

  public Optional<Piece> getCapturedPiece() {
    return capturedPiece;
  }
}
