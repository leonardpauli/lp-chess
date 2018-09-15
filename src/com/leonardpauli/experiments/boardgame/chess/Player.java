package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


class Player {
	String name;
	Color color;

	public Home home;
	bool alive = true;
	List<Piece> pieces = new ArrayList<Piece>();

	Player(String name, Color color) {
		this.name = name; this.color = color;
	}

	public Piece addPiece(Piece piece) {
		pieces.add(piece);
		piece.owner = this;
		return Piece;
	}
}
