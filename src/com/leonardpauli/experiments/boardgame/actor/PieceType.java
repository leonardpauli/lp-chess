package com.leonardpauli.experiments.boardgame.actor;

import com.leonardpauli.experiments.boardgame.board.movement.MovementType;
import com.leonardpauli.experiments.boardgame.util.Color;

public enum PieceType {
	KING ("King", 20, "♚", "♔", new MovementType[]{MovementType.ONE_STEP, MovementType.CASTLING}),
	QUEEN ("Queen", 9, "♛", "♕", new MovementType[]{MovementType.STRAIGHT, MovementType.DIAGONAL}),
	ROOK ("Rook", 5, "♜", "♖", new MovementType[]{MovementType.STRAIGHT}),
	KNIGHT ("Knight", 3, "♞", "♘", new MovementType[]{MovementType.LMOVE}),
	BISHOP ("Bishop", 3, "♝", "♗", new MovementType[]{MovementType.DIAGONAL}),
	PAWN ("Pawn", 1, "♟", "♙", new MovementType[]{MovementType.FORWARD_ONE, MovementType.FORWARD_TWO_FROM_HOME, MovementType.ENPASSANT, MovementType.PROMOTION});

	public String title;
	private int defaultValue = 0;
	private String defaultChar = "◆︎";
	private String defaultCharBlack = "◇";
	public final MovementType[] movementTypes;

	private PieceType(
		String title, int defaultValue,
		String defaultChar, String defaultCharBlack,
		MovementType[] movementTypes) {
		this.title = title;
		this.defaultValue = defaultValue;
		this.defaultChar = defaultChar;
		this.defaultCharBlack = defaultCharBlack;
		this.movementTypes = movementTypes;
	}


	// string

	public int getValue() { return defaultValue; }
	public String toChar(Color color) {
		return color == Color.black? defaultCharBlack: defaultChar;
	}
	public String getLetter() { return title.substring(0, 1); }
}
