package com.leonardpauli.experiments.boardgame.board;

import com.leonardpauli.experiments.boardgame.actor.Home;
import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.layout.Layout;
import com.leonardpauli.experiments.boardgame.board.layout.LayoutSquare;
import com.leonardpauli.experiments.boardgame.board.layout.PrinterSquare;
import com.leonardpauli.experiments.boardgame.board.movement.CastlingMovement;
import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.board.movement.MovementProcessor;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.game.notation.Move;
import com.leonardpauli.experiments.boardgame.util.Size;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.*;

public class Board {
  public List<Tile> tilesAll = new ArrayList<>();
  public Tile[][] tiles;
  public final MovementProcessor movement;
  public final Layout layout;
  private HashMap<Tile, Marker> tileMarks = new HashMap<>();

  public static enum Marker {
    AVAILABLE,
    UNSAFE_AVAILABLE,
    UNSAFE;

    public boolean isUnsafe() {
      return this == UNSAFE || this == UNSAFE_AVAILABLE;
    }

    public boolean isAvailable() {
      return this == AVAILABLE || this == UNSAFE_AVAILABLE;
    }
  }

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

  public Optional<Movement> getMovementForNotation(String notation, Player currentPlayer) {
    List<Movement> movements = getMovementsForNotation(notation, currentPlayer);
    if (movements.size() == 0) return Optional.empty();
    if (movements.size() > 1)
      System.out.println("getMovementForNotation got many; " + Util.objectToString(movements));
    return Optional.of(movements.get(0));
  }

  public List<Movement> getMovementsForNotation(String notation, Player currentPlayer) {

    Optional<Move> move = Move.fromString(notation);
    if (!move.isPresent()) return new ArrayList<>();

    return getMovementsForMove(move.get(), currentPlayer);
  }

  public List<Movement> getMovementsForMove(Move move, Player currentPlayer) {
    List<Movement> movements = new ArrayList<>();
    Move.Config c = move.getConfig();

    for (int x = c.origin.x.orElse(0); x <= c.origin.x.orElse(tiles.length - 1); x++) {
      for (int y = c.origin.y.orElse(0); y <= c.origin.y.orElse(tiles.length - 1); y++) {
        Tile t = tiles[x][y];
        if (!t.hasPiece()) continue;
        if (!t.getPiece().owner.equals(currentPlayer)) continue;
        if (c.type.isPresent() && t.getPiece().type != c.type.get()) continue;
        List<Movement> ms = movement.getAvailable(t.getPiece());
        if (!c.target.isEmpty())
          ms.removeIf(
              m ->
                  (c.target.hasX() && m.edge.target.position.x != c.target.getX(0))
                      || (c.target.hasY() && m.edge.target.position.y != c.target.getY(0))
                      || (c.isCapture
                          && (!m.edge.target.hasPiece()
                              || m.edge.target.getPiece().owner.equals(currentPlayer))));
        ms.removeIf(
            m ->
                (c.isCastling
                    && (!(m instanceof CastlingMovement)
                        || (c.isCastlingKingside != ((CastlingMovement) m).isKingside()))));
        movements.addAll(ms);
      }
    }

    return movements;
  }

  // marks

  public void setTileMarksFromAvailableMovements(
      Movement[] movements, Movement[] otherPlayersMovements) {
    clearTileMarks();
    for (Movement m : otherPlayersMovements) {
      tileMarks.put(m.edge.target, Marker.UNSAFE);
    }
    for (Movement m : movements) {
      boolean unsafe = tileMarks.get(m.edge.target) == Marker.UNSAFE;
      tileMarks.put(m.edge.target, unsafe ? Marker.UNSAFE_AVAILABLE : Marker.AVAILABLE);
    }
  }

  public void setTileMarksFromAvailableMovements(Movement[] movements) {
    clearTileMarks();
    for (Movement m : movements) {
      tileMarks.put(m.edge.target, Marker.AVAILABLE);
    }
  }

  public void clearTileMarks() {
    tileMarks.clear();
  }

  public Optional<Marker> getMarkerForTile(Tile tile) {
    return tileMarks.containsKey(tile) ? Optional.of(tileMarks.get(tile)) : Optional.empty();
  }

  // utils

  public String toString(PrinterSquare.Style style) {
    return layout.getPrinter().boardToString(this, style);
  }

  public String toString() {
    return toString(PrinterSquare.Style.PRETTY);
  }
}
