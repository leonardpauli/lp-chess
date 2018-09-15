package com.leonardpauli.experiments.boardgame.chess;

class Tile {
	public final Position position;
	public final Color color;
	public Piece piece;
	private Edge[][] edgeMap;

	// eg. = 2 for a game variant with 3 kingdoms
	private final int maxNrEdgesOfSameType = 1;

	Tile(Position position) {
		this.position = position;
		this.color = position.x%2==position.y%2? Color.black: Color.white;
	}


	// edges

	public Edge[] getEdges(EdgeType type) {
		return edgeMap[type.ordinal()];
	}
	void setEdges(Edge[] edges) {
		int edgeTypeCount = EdgeType.values().length;
		Edge[][] map = Edge[edgeTypeCount][];
		int[] mapSizes = int[edgeTypeCount];

		for (EdgeType t : EdgeType.values()) {
			int typeNr = t.ordinal();
			int maxSize = maxNrEdgesOfSameType;
			map[typeNr] = new Edge[maxSize];
		}
		for (Edge e : edges) {
			int typeNr = e.type.ordinal();
			map[typeNr][mapSizes[typeNr]++] = e;
		}

		Edge[][] mapFinal = Edge[edgeTypeCount][];
		for (EdgeType t : EdgeType.values()) {
			int typeNr = t.ordinal();
			int actualSize = mapSizes[typeNr];
			mapFinal[typeNr] = Arrays.copyOf(map[typeNr], actualSize);
		}

		edgeMap = mapFinal;
	}


	// string

	String toCharPlain() { return color == Color.black? " ": ".︎"; }
	public String toChar() { return piece==null? toCharPlain(): piece.toChar(p); }
	public String toCharPretty() { return piece==null? toCharPlain(): piece.toCharPretty(); }
}
