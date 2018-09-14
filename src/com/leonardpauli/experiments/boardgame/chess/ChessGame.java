package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


public class ChessGame {
	Player[] players = new Player[2]; // order determines play order
	Board board;
	List<Round> rounds = new ArrayList<Round>();
	List<Piece> pieces = new ArrayList<Piece>();

	public static void ChessGame() {
		this.players = [
			new Player("White", Color.white),
			new Player("Black", Color.black),
		];
		this.board = new Board();
		resetPieces();
	}

	public Round currentRound() {
		return rounds.get(rounds.size()-1);
	}
	public Player getCurrentPlayer() {
		Round round = currentRound();
		return players[round.moves.size() % players.length];
	}


	public void resetPieces() { /* ... */ }
}
