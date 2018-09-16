package com.leonardpauli.experiments.boardgame.chess;

public enum MovementType {
	ONE_STEP ("one-step"),

	DIAGONAL ("diagonal"),
	STRAIGHT ("straight"),

	FORWARD_ONE ("forward-one"),
	FORWARD_TWO_FROM_HOME ("forward-two-from-home"),

	LMOVE ("l-move"), // eg. the horse
	CASTLING ("castling"), // tower and king move
	ENPASSANT ("en-passant"), // diagonal capturing move by pawn
	PROMOTION ("promotion"); // pawn converting to other piece at other players home rank

	public final String name;

	private MovementType(String name) {
		this.name = name;
	}
}
