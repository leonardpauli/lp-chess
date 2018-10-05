package com.leonardpauli.experiments.boardgame.board.layout;

import com.leonardpauli.experiments.boardgame.actor.Home;
import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import javafx.geometry.Point2D;

import java.util.Iterator;

public interface Layout {
  void setupTiles(Board board);

  void setupTileEdges(Board board) throws Exception;

  boolean tileExistsAt(Board board, Position position);

  Tile tileAt(Board board, Position position) throws InvalidMoveException;

  Iterator<Home> getPlayerHomes(Board board);

  Printer getPrinter();

  Point2D[] getNormalizedCornersForTile(Tile tile);
}
