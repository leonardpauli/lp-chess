package com.leonardpauli.experiments.boardgame.chess;

class Color {
	public static final black = new Color("black");
	public static final white = new Color("white");

	String name;
	Color(String name) {this.name = name;}
}
