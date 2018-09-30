package com.leonardpauli.experiments.boardgame.board.layout;

import com.leonardpauli.experiments.boardgame.actor.Home;
import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;
import com.leonardpauli.experiments.boardgame.board.tile.EdgeType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.util.Size;
import javafx.geometry.Point2D;

import java.util.Arrays;
import java.util.Iterator;

public class LayoutSquare implements Layout {

  private Size size;
  private Printer printer;

  public LayoutSquare(Size size) {
    this.size = size;
    this.printer = new PrinterSquare(this);
  }

  public void setupTiles(Board board) {
    board.tiles = new Tile[size.x][size.y];
    for (int x = 0; x < size.x; x++) {
      for (int y = 0; y < size.y; y++) {
        Position position = new Position(x, y);
        Tile tile = new Tile(position);
        board.tiles[x][y] = tile;
        board.tilesAll.add(tile);
      }
    }
  }

  public void setupTileEdges(Board board) throws Exception {
    for (int x = 0; x < size.x; x++) {
      for (int y = 0; y < size.y; y++) {
        Tile tile = board.tiles[x][y];
        tile.setEdges(getTileEdges(board, tile));
      }
    }
  }

  private Edge[] getTileEdges(Board board, Tile tile) throws InvalidMoveException {
    int maxSize = EdgeType.values().length;
    Edge[] edges = new Edge[maxSize];
    int i = 0;
    for (EdgeType type : EdgeType.values()) {
      Position position = new Position(tile.position.add(type.direction));
      if (tileExistsAt(board, position)) edges[i++] = new Edge(tile, type, tileAt(board, position));
    }
    int actualSize = i;
    return Arrays.copyOf(edges, actualSize);
  }

  public boolean tileExistsAt(Board board, Position position) {
    boolean inBounds =
        0 <= position.x
            && position.x < board.tiles.length
            && 0 <= position.y
            && position.y < board.tiles[position.x].length;
    return inBounds;
  }

  public Tile tileAt(Board board, Position position) throws InvalidMoveException {
    if (!tileExistsAt(board, position))
      throw new InvalidMoveException(InvalidMoveException.Type.DESTINATION_NOT_FOUND);

    return board.tiles[position.x][position.y];
  }

  @Override
  public Iterator<Home> getPlayerHomes(Board board) {
    int midXLeft = size.x / 2 - 1;
    int topY = size.y - 1;
    return new Iterator<>() {
      private int i = 0;
      private int iMax = 2;

      @Override
      public boolean hasNext() {
        return i < iMax;
      }

      @Override
      public Home next() {
        try {
          return getNext(i++);
        } catch (GameException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }

      private Home getNext(int i) throws GameException {
        return i == 0
            ? new Home(new Position(midXLeft, 0), new Position(midXLeft, 1), board)
            : new Home(
                new Position(midXLeft + 1, topY), new Position(midXLeft + 1, topY - 1), board);
      }
    };
  }

  public Size getSize() {
    return size;
  }

  @Override
  public Printer getPrinter() {
    return printer;
  }

  @Override
  public Point2D[] getNormalizedCornersForTile(Tile tile) {
    Point2D start = pointDivide(tile.position.getPoint2D(), size.getPoint2D());
    Point2D end = pointDivide(tile.position.getPoint2D().add(1, 1), size.getPoint2D());

    return new Point2D[] {
      new Point2D(start.getX(), start.getY()),
      new Point2D(end.getX(), start.getY()),
      new Point2D(end.getX(), end.getY()),
      new Point2D(start.getX(), end.getY())
    };
  }

  private Point2D pointDivide(Point2D a, Point2D b) {
    return new Point2D(a.getX() / b.getX(), a.getY() / b.getY());
  }
}
