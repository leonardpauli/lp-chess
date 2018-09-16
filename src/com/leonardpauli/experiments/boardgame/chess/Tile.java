package com.leonardpauli.experiments.boardgame.chess;

import java.util.Arrays;

class Tile {
	public final Position position;
	private final Color color;
	
	private Piece piece;
	private Edge[][] edgeMap;
	private Edge[] edges;

	// eg. = 2 for a game variant with 3 kingdoms
	private static final int maxNrEdgesOfSameType = 1;

	Tile(Position position) {
		this.position = position;
		this.color = position.x%2==position.y%2? Color.black: Color.white;
	}


	// piece

	void removePiece() { this.piece.tile = null; this.piece = null; }
	void setPiece(Piece piece) {
		if (hasPiece()) removePiece();
		if (piece.tile!=null) piece.tile.removePiece();
		this.piece = piece;
		piece.tile = this;
	}
	public Piece getPiece() { return piece; }
	public boolean hasPiece() { return piece!=null; }
	public boolean isOccupiedBy(Player player) {
		return hasPiece() && getPiece().owner.equals(player);
	}


	// edges

	public Edge[] getEdges() { return edges; }
	public Edge[] getEdges(EdgeType type) {
		return edgeMap[type.ordinal()];
	}

	void setEdges(Edge[] edges) {
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
		int i = 0; for (Edge edge : edges) tiles[i++] = edge.target;
		return tiles;
	}
	public Tile getFirstRelative(EdgeType[] path) throws ChessException {
		Tile tile = this;
		for (EdgeType segment : path) {
			Tile[] tiles = tile.getRelative(segment);
			if (tiles.length==0) throw new ChessException("path is broken");
			tile = tiles[0];
		}
		return tile;
	}
	public Tile[] getRelative(EdgeType[] path) throws ChessException {
		throw new ChessException("not implemented");
	}


	// string

	String toCharPlain() { return color == Color.black? " ": "."; }
	public String toChar() { return piece==null? toCharPlain(): piece.toChar(); }
	public String toCharPretty() { return piece==null? toCharPlain(): piece.toCharPretty(); }
}
