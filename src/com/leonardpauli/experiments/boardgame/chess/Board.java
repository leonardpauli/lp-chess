package com.leonardpauli.experiments.boardgame.chess;


class Board {
	public Tile[][] tiles;
	public final Size size = new Size(8, 8);
	public final BoardMovementProcessor movement;

	Board() {
		movement = new BoardMovementProcessor(this);
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

	public Tile placePiece(Piece piece, Tile tile) throws InvalidMoveException {
		if (tile.piece!=null) throw InvalidMoveException(
			InvalidMoveException.Type.DESTINATION_OCCUPIED);

		piece.position = position;
		tile.piece = piece;

		return tile;
	}
	public Tile placePiece(Piece piece, Position position) throws InvalidMoveException {
		Tile tile = getTileAt(position);
		return placePiece(piece, tile);
	}

	public Piece getPieceAt(Position position) throws InvalidMoveException {
		return getTileAt(position).piece;
	}
	public Piece getPieceAt(String code) throws ChessException, InvalidMoveException {
		return getPieceAt(new Position(code));
	}


	// utils

	public String toString(Printer.Style style) { return Printer.boardToString(this, style); }
	public String toString() { return toString(Printer.Style.PRETTY); }
}
