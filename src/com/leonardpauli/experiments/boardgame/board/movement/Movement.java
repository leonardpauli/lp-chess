package com.leonardpauli.experiments.boardgame.board.movement;


import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;

public class Movement {
	MovementType type;
	public Edge edge;

	Piece capturedPiece;
	int value;

	public Movement(MovementType type, Edge edge) {
		this.type = type;
		this.edge = edge;
	}

	public void setCapturedPiece(Piece piece) {
		this.capturedPiece = piece;
		this.value = piece.type.getValue();
	}
}


