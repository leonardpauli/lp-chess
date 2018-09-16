package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


class Player {
	public String name;
	Color color;

	public Home home;
	bool alive = true;
	List<Piece> pieces = new ArrayList<Piece>();

	Player(String name, Color color) {
		this.name = name; this.color = color;
	}
	Player(int ordinal) {
		if (ordinal==0) {
			color = Color.white;
			name = "White";
		} else if (ordinal==1) {
			color = Color.black;
			name = "Black";
		} else {
			color = Color.fromHSL((float) ordinal / 10, 0.5, 0.5);
			name = "Player " + Integer.toString(1 + ordinal);
		}
	}


	// piece

	public Piece addPiece(Piece piece) {
		pieces.add(piece);
		piece.owner = this;
		return Piece;
	}

}
