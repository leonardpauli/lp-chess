package com.leonardpauli.experiments.boardgame.chess;

public class Piece {
	PieceType type;
	Position position;
	Player owner;

	Piece(Type type) {
		this.type = type;
	}

	public Color getColor() { return owner.color; }
	public String toChar() { return type.toChar(getColor()); }
}

public class PieceType {
	public static String name;
	static int defaultValue;
	static String defaultChar = "◆︎";
	static String defaultCharBlack = "◇";

	public static getValue() { return defaultValue; }
	public static String toChar(Color color) {
		return color == Color.black? defaultCharBlack: defaultChar;
	}
	public static String getLetter() { return name.substring(0, 1); }
}


class King extends PieceType {
	public static String name = "King";
	public static int defaultValue = 20;
	public static String defaultChar = "♚";
	public static String defaultCharBlack = "♔";
}
class Queen extends PieceType {
	public static String name = "Queen";
	public static int defaultValue = 9;
	public static String defaultChar = "♛";
	public static String defaultCharBlack = "♕";
}
class Rook extends PieceType {
	public static String name = "Rook";
	public static int defaultValue = 5;
	public static String defaultChar = "♜";
	public static String defaultCharBlack = "♖";
}
class Knight extends PieceType {
	public static String name = "Knight";
	public static int defaultValue = 3;
	public static String defaultChar = "♞";
	public static String defaultCharBlack = "♘";
}
class Bishop extends PieceType {
	public static String name = "Bishop";
	public static int defaultValue = 3;
	public static String defaultChar = "♝";
	public static String defaultCharBlack = "♗";
}
class Pawn extends PieceType {
	public static String name = "Pawn";
	public static int defaultValue = 1;
	public static String defaultChar = "♟";
	public static String defaultCharBlack = "♙";
}
