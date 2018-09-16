package com.leonardpauli.experiments.boardgame.chess;

public class Piece {
	PieceType type;
	Tile tile;
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
	KING ("King", 20, "♚", "♔", {ONE_STEP, CASTLING}),
	QUEEN ("Queen", 9, "♛", "♕", {STRAIGHT, DIAGONAL}),
	ROOK ("Rook", 5, "♜", "♖", {STRAIGHT}),
	KNIGHT ("Knight", 3, "♞", "♘", {LMOVE}),
	BISHOP ("Bishop", 3, "♝", "♗", {DIAGONAL}),
	PAWN ("Pawn", 1, "♟", "♙", {FORWARD_ONE, FORWARD_TWO_AT_START, ENPASSANT, PROMOTION});

	public static String title;
	private int defaultValue = 0;
	private String defaultChar = "◆︎";
	private String defaultCharBlack = "◇";
	public final MovementType[] movementTypes;

	private PieceType(
		String title, int defaultValue,
		String defaultChar, String defaultCharBlack,
		MovementType movementTypes) {
		this.title = title;
		this.defaultValue = defaultValue;
		this.defaultChar = defaultChar;
		this.defaultCharBlack = defaultCharBlack;
		this.movementTypes = movementTypes;
	}


	// string

	public static getValue() { return defaultValue; }
	public static String toChar(Color color) {
		return color == Color.black? defaultCharBlack: defaultChar;
	}
	public static String getLetter() { return title.substring(0, 1); }
}
