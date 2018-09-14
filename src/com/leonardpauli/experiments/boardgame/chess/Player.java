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

class Color {
	public static final black = new Color("black");
	public static final white = new Color("white");

	String name;
	Color(String name) {this.name = name;}
}

class Home {
	public final Position position;
	public final Position positionForward;
	public final Position delta;

	Home(Position position, Position positionForward) {
		this.position = position;
		this.positionForward = positionForward;
		this.delta = positionForward.sub(position);
	}
}