package com.leonardpauli.experiments.boardgame.board.movement;

import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;
import com.leonardpauli.experiments.boardgame.board.tile.EdgeType;
import com.leonardpauli.experiments.boardgame.game.GameException;

public class CastlingMovement extends Movement {
  public final Edge rookEdge;
  public final EdgeType direction;

  public CastlingMovement(Edge edge, Edge rookEdge, EdgeType direction) {
    super(MovementType.CASTLING, edge);
    this.rookEdge = rookEdge;
    this.direction = direction;
  }

  private boolean isKingsideFor(Player player) {
    try {
      return player.home.getEdgeForward().type.getTurned(3) == direction;
    } catch (GameException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isKingside() {
    return direction == EdgeType.RIGHT; // isKingsideFor(edge.source.getPiece().owner);
  }
}
