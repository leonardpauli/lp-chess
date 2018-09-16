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

	void setCapturedPiece(Piece piece) {
		this.capturedPiece = piece;
		this.value = piece.type.getValue();
	}
}


