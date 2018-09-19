package com.leonardpauli.experiments.boardgame.actor;

import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.util.Color;

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

	public boolean isAlive() { return tile!=null; }

	public Color getColor() { return owner.color; }
	public String toChar() { return getColor()==Color.black
		? type.getLetter().toLowerCase()
		: type.getLetter().toUpperCase();
	}
	public String toCharPretty() { return type.toChar(getColor()); }
	public String toString() {
		return "Piece("+type.title+"){"+
						"tile: "+(tile!=null? tile.position.toString(): null)+", "+
						"owner: "+(owner!=null? owner.name: null)+"}";
	}
}

