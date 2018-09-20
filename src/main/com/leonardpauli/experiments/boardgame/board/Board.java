package com.leonardpauli.experiments.boardgame.board;

import com.leonardpauli.experiments.boardgame.actor.Home;
import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.layout.Layout;
import com.leonardpauli.experiments.boardgame.board.layout.LayoutSquare;
import com.leonardpauli.experiments.boardgame.board.layout.PrinterSquare;
import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.board.movement.MovementProcessor;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.util.Size;

import java.util.Iterator;

public class Board {
  public Tile[][] tiles;
  public final MovementProcessor movement;
  private final Layout layout;

  public Board() throws Exception {
    layout = new LayoutSquare(new Size(8, 8));
    movement = new MovementProcessor(this);
    layout.setupTiles(this);
    layout.setupTileEdges(this);
  }

  // home

  public Iterator<Home> getPlayerHomes() {
    return layout.getPlayerHomes(this);
  }

  // tile

  public boolean tileExistsAt(Position position) {
    return layout.tileExistsAt(this, position);
  }

  public Tile tileAt(Position position) throws InvalidMoveException {
    return layout.tileAt(this, position);
  }

  // piece

  public void removePiece(Piece piece) {
    if (piece.tile != null) piece.tile.removePiece();
  }

  public Tile placePiece(Piece piece, Tile tile) throws InvalidMoveException {
    if (tile.hasPiece())
      throw new InvalidMoveException(InvalidMoveException.Type.DESTINATION_OCCUPIED);

    tile.setPiece(piece);

    return tile;
  }

  public Tile placePiece(Piece piece, Position position) throws InvalidMoveException {
    Tile tile = tileAt(position);
    return placePiece(piece, tile);
  }

  public Piece pieceAt(Position position) throws InvalidMoveException {
    return tileAt(position).getPiece();
  }

  public Piece pieceAt(String code) throws GameException {
    return pieceAt(Position.fromString(code));
  }

  // movement

  public Movement getMovementForNotation(String notation, Player currentPlayer) {
    return null;
  }

  // utils

  public String toString(PrinterSquare.Style style) {
    return layout.getPrinter().boardToString(this, style);
  }

  public String toString() {
    return toString(PrinterSquare.Style.PRETTY);
  }
}
