package com.leonardpauli.experiments.boardgame.chess;


class Player {
	String name;
	Color color;

	public Home home;

	Player(String name, Color color) {
		this.name = name; this.color = color;
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