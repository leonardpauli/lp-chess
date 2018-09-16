package com.leonardpauli.experiments.boardgame.chess;


import java.util.Arrays;

class Board {
	public Tile[][] tiles;
	public final Size size = new Size(8, 8);
	public final BoardMovementProcessor movement;

	Board() throws Exception {
		movement = new BoardMovementProcessor(this);
		setupTiles();
		setupTileEdges();
	}


	// tile

	private void setupTiles() {
		tiles = new Tile[size.x][size.y];
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Position position = new Position(x, y);
				tiles[x][y] = new Tile(position);
			}
		}
	}
	private void setupTileEdges() throws Exception {
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Tile tile = tiles[x][y];
				tile.setEdges(getTileEdges(tile));
			}
		}
	}

	public boolean tileExistsAt(Position position) {
		boolean inBounds =
			0 <= position.x && position.x < tiles.length &&
			0 <= position.y && position.y < tiles[position.x].length;
		return inBounds;
	}

	public Tile tileAt(Position position) throws InvalidMoveException {
		if (!tileExistsAt(position)) throw new InvalidMoveException(
			InvalidMoveException.Type.DESTINATION_NOT_FOUND);

		return tiles[position.x][position.y];
	}

	private Edge[] getTileEdges(Tile tile) throws InvalidMoveException {
		int maxSize = EdgeType.values().length;
		Edge[] edges = new Edge[maxSize];
		int i = 0;
		for (EdgeType type : EdgeType.values()) {
			Position position = new Position(tile.position.add(type.direction));
			if (tileExistsAt(position))
				edges[i++] = new Edge(tile, type, tileAt(position));
		}
		int actualSize = i;
		return Arrays.copyOf(edges, actualSize);
	}


	// piece

	public void removePiece(Piece piece) {
		if (piece.tile!=null) piece.tile.removePiece();
	}

	public Tile placePiece(Piece piece, Tile tile) throws InvalidMoveException {
		if (tile.hasPiece()) throw new InvalidMoveException(
			InvalidMoveException.Type.DESTINATION_OCCUPIED);

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
	public Piece pieceAt(String code) throws ChessException {
		return pieceAt(Position.fromString(code));
	}


	// utils

	public String toString(Printer.Style style) { return Printer.boardToString(this, style); }
	public String toString() { return toString(Printer.Style.PRETTY); }
}
