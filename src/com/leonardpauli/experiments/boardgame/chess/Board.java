package com.leonardpauli.experiments.boardgame.chess;


class Board {
	public Tile[][] tiles;
	public final Size size = new Size(8, 8);

	Board() {
		setupTiles();
		setupTileEdges();
	}


	// tile

	private void setupTiles() {
		tiles = new Tiles[size.x][size.y];
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Position position = new Position(x, y);
				Tile tile = tiles[x][y] = new Tile(position);
			}
		}
	}
	private void setupTileEdges() {
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Position position = new Position(x, y);
				Tile tile = tiles[x][y];
				tile.setEdges(getTileEdges(tile));
			}
		}
	}

	public bool tileExistsAt(Position position) {
		bool inBounds =
			0 <= position.x && position.x < tiles.length &&
			0 <= position.y && position.y < tiles[position.x].length;
		return inBounds;
	}

	public Tile getTileAt(Position position) throws InvalidMoveException {
		if (!tileExistsAt(position)) throw InvalidMoveException(
			InvalidMoveException.Type.DESTINATION_NOT_FOUND);

		return tiles[position.x][position.y];
	}

	private Edge[] getTileEdges(Tile tile) {
		int maxSize = EdgeType.values().length;
		Edge[] edges = new Edge[maxSize];
		int i = 0;
		for (EdgeType type : EdgeType.values()) {
			if (tileExistsAt(type.direction))
				edges[i++] = new Edge(tile, type, getTileAt(position));
		}
		int actualSize = i;
		return Arrays.copyOf(edges, actualSize);
	}


	// piece

	public void removePiece(Piece piece) {
		if (piece.position)
			tiles[piece.position.x][piece.position.y].piece = null;
		piece.position = null;
	}

	public void placePiece(Piece piece, Position position) throws InvalidMoveException {
		Tile tile = getTileAt(position);

		if (tile.piece!=null) throw InvalidMoveException(
			InvalidMoveException.Type.DESTINATION_OCCUPIED);

		piece.position = position;
		tile.piece = piece;
	}

	public Piece getPieceAt(Position position) throws InvalidMoveException {
		return getTileAt(position).piece;
	}
	public Piece getPieceAt(String code) throws ChessException, InvalidMoveException {
		return getPieceAt(new Position(code));
	}
	

	// other

	public List<Movement> getAvailableMovements(Piece piece, Position destination) {
		List<Movement> movements = new ArrayList<Movement>();

		// TODO

		return movements;
	}


	// utils

	public String toString(Style style) { return Printer.boardToString(this, style); }
	public String toString() { return toString(Printer.Style.PRETTY); }
}


class Position extends Point {
	Position(int x, int y) {super(x, y);}
	Position(String code) throws ChessException {
		if (code.length() != 2) throw new ChessException(
			"code.length has to be 2, was "+Integer.toString(code.length()));
		int x = ((int)code.charAt(0)) - 65;
		int y = Integer.parseInt(code.substring(1,2));
		super(x, y);
	}
	public String toString() { return colString() + rowString(); }
	public String colString() { return Character.toString((char) (x + 65)); }
	public String rowString() { return Integer.toString(y+1); }
}


class Tile {
	public final Position position;
	public final Color color;
	public Piece piece;
	private Edge[][] edgeMap;

	// eg. 2 in variant with 3 kingdoms
	private final int maxNrEdgesOfSameType = 1;

	Tile(Position position) {
		this.position = position;
		this.color = position.x%2==position.y%2? Color.black: Color.white;
	}

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

	String toCharPlain() { return color == Color.black? " ": ".︎"; }
	public String toChar() { return piece==null? toCharPlain(): piece.toChar(p); }
	public String toCharPretty() { return piece==null? toCharPlain(): piece.toCharPretty(); }
}
