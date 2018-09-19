package com.leonardpauli.experiments.boardgame.actor;

import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.util.Color;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


class Player {
	public String name;
	Color color;

	public Home home;
	boolean alive = true;
	List<Piece> pieces = new ArrayList<Piece>();

	Player(String name, Color color) {
		this.name = name; this.color = color;
	}
	Player(int ordinal) {
		if (ordinal==0) {
			color = Color.white;
			name = "White";
		} else if (ordinal==1) {
			color = Color.black;
			name = "Black";
		} else {
			color = Color.fromHSL( ordinal / 10f, 0.5f, 0.5f);
			name = "Player " + Integer.toString(1 + ordinal);
		}
	}


	// piece

	public Piece addPiece(Piece piece) {
		pieces.add(piece);
		piece.owner = this;
		return piece;
	}

	public Piece[] getAlivePieces() {
		Piece[] alivePieces = new Piece[pieces.size()];
		int i = 0;
		for (Piece piece : pieces) if (piece.isAlive()) alivePieces[i++] = piece;
		return Arrays.copyOf(alivePieces, i);
	}

	public Piece getFirstMovablePiece(Board board) throws GameException {
		for (Piece piece : getAlivePieces()) {
			List<Movement> movements = board.movement.getAvailable(piece);
			if (movements.size()>0) return piece;
		}
		throw new GameException("no movable pieces for that player");
	}

}
