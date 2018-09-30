package com.leonardpauli.experiments.boardgame.board.tile;

import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.util.Color;

import java.util.Arrays;

public class Tile {
  public final Position position;
  public final Color color;

  private Piece piece;
  private Edge[][] edgeMap;
  private Edge[] edges;

  // eg. = 2 for a game variant with 3 kingdoms
  private static final int maxNrEdgesOfSameType = 1;

  public Tile(Position position) {
    this.position = position;
    this.color = position.x % 2 == position.y % 2 ? Color.black : Color.white;
  }

  // piece

  public void removePiece() {
    this.piece.tile = null;
    this.piece = null;
  }

  public void setPiece(Piece piece) {
    if (hasPiece()) removePiece();
    if (piece.tile != null) piece.tile.removePiece();
    this.piece = piece;
    piece.tile = this;
  }

  public Piece getPiece() {
    return piece;
  }

  public boolean hasPiece() {
    return piece != null;
  }

  public boolean isOccupiedBy(Player player) {
    return hasPiece() && getPiece().owner.equals(player);
  }

  // edges

  public Edge[] getEdges() {
    return edges;
  }

  public Edge[] getEdges(EdgeType type) {
    return edgeMap[type.ordinal()];
  }

  public void setEdges(Edge[] edges) {
    int edgeTypeCount = EdgeType.values().length;
    Edge[][] map = new Edge[edgeTypeCount][];
    int[] mapSizes = new int[edgeTypeCount];

    for (EdgeType t : EdgeType.values()) {
      int typeNr = t.ordinal();
      int maxSize = Tile.maxNrEdgesOfSameType;
      map[typeNr] = new Edge[maxSize];
    }
    for (Edge e : edges) {
      int typeNr = e.type.ordinal();
      map[typeNr][mapSizes[typeNr]++] = e;
    }

    Edge[][] mapFinal = new Edge[edgeTypeCount][];
    for (EdgeType t : EdgeType.values()) {
      int typeNr = t.ordinal();
      int actualSize = mapSizes[typeNr];
      mapFinal[typeNr] = Arrays.copyOf(map[typeNr], actualSize);
    }

    edgeMap = mapFinal;
    this.edges = edges;
  }

  // path

  public Tile[] getRelative(EdgeType pathSegment) {
    Edge[] edges = getEdges(pathSegment);
    Tile[] tiles = new Tile[edges.length];
    int i = 0;
    for (Edge edge : edges) tiles[i++] = edge.target;
    return tiles;
  }

  public Tile getFirstRelative(EdgeType[] path) throws GameException {
    Tile tile = this;
    for (EdgeType segment : path) {
      Tile[] tiles = tile.getRelative(segment);
      if (tiles.length == 0) throw new GameException("path is broken");
      tile = tiles[0];
    }
    return tile;
  }

  public Tile[] getRelative(EdgeType[] path) throws GameException {
    throw new GameException("not implemented");
  }

  // string

  String toCharPlain() {
    return color == Color.black ? " " : ".";
  }

  public String toChar() {
    return piece == null ? toCharPlain() : piece.toChar();
  }

  public String toCharPretty() {
    return piece == null ? toCharPlain() : piece.toCharPretty();
  }
}
