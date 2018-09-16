package com.leonardpauli.experiments.boardgame.chess;

import static com.leonardpauli.experiments.boardgame.chess.MovementType.*;

public class Piece {
	PieceType type;
	Tile tile;
	Player owner;
	private Tile homeTile;

	Piece(PieceType type) {
		this.type = type;
	}

	public boolean isAtHome() { return tile==homeTile; }
	public void setHome(Tile tile) { homeTile = tile; }

	public Color getColor() { return owner.color; }
	public String toChar() { return getColor()==Color.black
		? type.getLetter().toLowerCase()
		: type.getLetter().toUpperCase();
	}
	public String toCharPretty() { return type.toChar(getColor()); }
}

