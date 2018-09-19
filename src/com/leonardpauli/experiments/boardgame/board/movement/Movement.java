package com.leonardpauli.experiments.boardgame.board.movement;


import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;

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


