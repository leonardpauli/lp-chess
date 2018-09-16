package com.leonardpauli.experiments.boardgame.chess;


public class Movement {
	MovementType type;
	Edge edge;

	Piece capturedPiece;
	int value;

	Movement(MovementType type, Edge edge) {
		this.type = type;
		this.edge = edge;
	}

	setCapturedPiece(Piece piece) {
		this.capturedPiece = piece;
		this.value = piece.type.getValue();
	}
}

public enum MovementType {
	ONE_STEP ("one-step"),

	DIAGONAL ("diagonal"),
	STRAIGHT ("straight"),

	FORWARD_ONE ("forward-one"),
	FORWARD_TWO_FROM_HOME ("forward-two-from-home"),

	LMOVE ("l-move"),
	CASTLING ("castling"),
	ENPASSANT ("en-passant"),
	PROMOTION ("promotion");

	public final String name;

	private MovementType(String name) {
		this.name = name;
	}
}
