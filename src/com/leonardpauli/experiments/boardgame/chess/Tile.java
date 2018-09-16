package com.leonardpauli.experiments.boardgame.chess;

class Tile {
	public final Position position;
	public final Color color;
	
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

	public Piece getPiece() { return piece; }
	public bool hasPiece() { return piece!=null; }
	public bool isOccupiedBy(Player player) {
		return hasPiece() && getPiece().owner.equals(player);
	}


	// edges

	public Edge[] getEdges() { return edges; }
	public Edge[] getEdges(EdgeType type) {
		return edgeMap[type.ordinal()];
	}

	void setEdges(Edge[] edges) {
		int edgeTypeCount = EdgeType.values().length;
		Edge[][] map = Edge[edgeTypeCount][];
		int[] mapSizes = int[edgeTypeCount];

		for (EdgeType t : EdgeType.values()) {
			int typeNr = t.ordinal();
			int maxSize = Tile.maxNrEdgesOfSameType;
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
		this.edges = edges;
	}


	// path

	public Tile[] getRelative(EdgeType pathSegment) {
		Edge[] edges = getEdges(pathSegment);
		Tile[] tiles = new Tile[edges.length];
		int i = 0; for (Edge edge : edges) tiles[i++] = edge.target;
		return tiles;
	}
	public Tile getFirstRelative(EdgeType[] path) {
		Tile tile = this;
		for (EdgeType segment : path) {
			Tile[] tiles = tile.getRelative(segment);
			if (tiles.length==0) throw new ChessException("path is broken");
			tile = tiles[0];
		}
		return tile;
	}
	public Tile[] getRelative(EdgeType[] path) throws ChessException {
		new ChessException("not implemented");
	}


	// string

	String toCharPlain() { return color == Color.black? " ": ".︎"; }
	public String toChar() { return piece==null? toCharPlain(): piece.toChar(p); }
	public String toCharPretty() { return piece==null? toCharPlain(): piece.toCharPretty(); }
}
