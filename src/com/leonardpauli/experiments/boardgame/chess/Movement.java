package com.leonardpauli.experiments.boardgame.chess;


public class Movement {
	Piece capturedPiece;
	int value;
	public MovementType type;

	Movement(MovementType type) {
		this.type = type;
	}
}

public enum MovementType {
	DIAGONAL ("diagonal", [EdgeType.LEFT, EdgeType.RIGHT]),
	LEFT ("left"),
	RIGHT ("right"),
	FORWARD ("forward"),
	BACKWARDS ("backwards"),
	LMOVE ("l-move"),
	CASTLING ("castling"),
	ENPASSANT ("en-passant"),
	PROMOTION ("promotion");

	public final String name;
	public final EdgeType[] path;

	private MovementType(String name, EdgeType[] path) {
		this.name = name;
		this.path = path;
	}
}
