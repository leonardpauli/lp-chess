package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Move implements Token {
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

  String text;
  private static Pattern pattern = Pattern.compile("^([^\\d\\n])+");

  @Override
  public TokenizeResult getMatchResult(String str) {
    Matcher m = pattern.matcher(str);
    if (!m.find()) return new TokenizeResult();
    text = m.group();
    return new TokenizeResult(m.end());
  }
}
