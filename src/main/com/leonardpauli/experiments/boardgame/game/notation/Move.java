package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;

public class Move {
  public PieceType pieceType = PieceType.PAWN;

  // TODO: NaN or better way to signal unknown (partial in any of x/y)
  public Position source = new Position(-1, -1);
  public Position target = new Position(-1, -1);

  public boolean isEnpassant = false;
  public boolean isPromotion = false;
  public PieceType promotionTargetPieceType = null;
  public boolean isCastling = false;
  public boolean castlingQueenside = false; // kingside

  public AnnotationMove annotationMove = AnnotationMove.NONE;
  public AnnotationState annotationState = AnnotationState.NONE;
  public String comment = null;

  public boolean hasComment() {
    return comment != null;
  }
}
