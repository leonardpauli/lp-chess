package com.leonardpauli.experiments.boardgame.chess;

public class Piece {
	PieceType type;
	Position position;
	Player owner;

	Piece(PieceType type) {
		this.type = type;
	}

	public Color getColor() { return owner.color; }
	public String toChar() { return getColor()==Color.black
		? type.getLetter().toLowerCase()
		: type.getLetter().toUpperCase();
	}
	public String toCharPretty() { return type.toChar(getColor()); }
}

public enum PieceType {
	KING ("King", 20, "♚", "♔"),
	QUEEN ("Queen", 9, "♛", "♕"),
	ROOK ("Rook", 5, "♜", "♖"),
	KNIGHT ("Knight", 3, "♞", "♘"),
	BISHOP ("Bishop", 3, "♝", "♗"),
	PAWN ("Pawn", 1, "♟", "♙");

	public static String name;
	static int defaultValue;
	static String defaultChar = "◆︎";
	static String defaultCharBlack = "◇";

	private PieceType(
		String name, int defaultValue,
		String defaultChar, String defaultCharBlack) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.defaultChar = defaultChar;
		this.defaultCharBlack = defaultCharBlack;
	}

	public static getValue() { return defaultValue; }
	public static String toChar(Color color) {
		return color == Color.black? defaultCharBlack: defaultChar;
	}
	public static String getLetter() { return name.substring(0, 1); }
}
