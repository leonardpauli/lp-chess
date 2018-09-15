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
	Player(int ordinal) {
		this.color =
				ordinal == 0 ? Color.white
			: ordinal == 1 ? Color.black
			: Color.fromHSL((float) ordinal / 10, 0.5, 0.5);
		this.name = 
				ordinal == 0 ? "White"
			: ordinal == 1 ? "Black"
			: "Player " + Integer.toString(1 + ordinal);
	}


	// piece

	public Piece addPiece(Piece piece) {
		pieces.add(piece);
		piece.owner = this;
		return Piece;
	}

}
