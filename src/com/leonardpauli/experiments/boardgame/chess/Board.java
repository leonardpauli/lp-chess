package com.leonardpauli.experiments.boardgame.chess;


class Board {
	public Tile[][] tiles;
	public final Size size = new Size(8, 8);

	Board() {
		tiles = new Tiles[size.x][size.y];
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Position position = new Position(x, y);
				Tile tile = tiles[x][y] = new Tile(position);
			}
		}
	}

	public enum PrintStyle { Plain, Pretty, PrettyWithNumbers };
	public String toString(PrintStyle style) {
		StringBuilder sb = new StringBuilder("");
		bool pretty = style==PrintStyle.Pretty || style==PrintStyle.PrettyWithNumbers;
		bool numbers = style==PrintStyle.PrettyWithNumbers;
		
		if (pretty && numbers) sb.append("  ");
		if (pretty) sb.append("╭────────────────────────╮\n");

		for (int y = 0; y<size.y; x++) {
			if (pretty && numbers) sb.append(tiles[0][y].position.rowString()+" ");
			if (pretty) sb.append("│");

			for (int x = 0; x<size.x; x++) {
				Tile tile = tiles[x][y];
				if (pretty) sb.append(" ");
				sb.append(pretty ? tile.toCharPretty(): tile.toChar());
				if (pretty) sb.append(" ");
			}

			if (pretty) sb.append("│");
			sb.append("\n");
		}

		if (pretty && numbers) sb.append("  ");
		if (pretty) sb.append("╰────────────────────────╯\n");

		if (numbers) {
			sb.append("  ");
			if (pretty) sb.append(" ");
			for (int x = 0; x<size.x; x++) {
				Tile tile = tiles[x][0];
				if (pretty) sb.append(" ");
				sb.append(tile.position.colString());
				if (pretty) sb.append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
	public String toString() { return toString(PrintStyle.Pretty); }
}


class Position extends Point {
	Position(int x, int y) {super(x, y);}
	public String toString() { return colString() + rowString(); }
	public String colString() { return Character.toString((char) (x + 65)); }
	public String rowString() { return Integer.toString(y+1); }
}

class Tile {
	public final Position position;
	public final Color color;
	public Piece piece;

	Tile(Position position) {
		this.position = position;
		this.color = position.x%2==position.y%2? Color.black: Color.white;
	}

	String toCharPlain() { return color == Color.black? " ": ".︎"; }
	public String toChar() { return piece==null? toCharPlain(): piece.toChar(p); }
	public String toCharPretty() { return piece==null? toCharPlain(): piece.toCharPretty(); }
}
