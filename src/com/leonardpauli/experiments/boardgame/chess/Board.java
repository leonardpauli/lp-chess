package com.leonardpauli.experiments.boardgame.chess;


class Board {
	public Tile[][] tiles;
	Size size = new Size(8, 8);

	Board() {
		tiles = new Tiles[size.x][size.y];
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Position position = new Position(x, y);
				Tile tile = tiles[x][y] = new Tile(position);
			}
		}
	}
}

class Position extends Point {
	Position(int x, int y) {super(x, y);}
	public String toString() {
		return Character.toString((char) x + 65) + Integer.toString(y+1);
	}
}

class Tile {
	public final Position position;
	public final Color color;

	Tile(Position position) {
		this.position = position;
		this.color = position.x%2==position.y%2? Color.black: Color.white;
	}

	public String toChar() { return color == Color.black? "⬛": "︎⬜︎"; }
}
