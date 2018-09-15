package com.leonardpauli.experiments.boardgame.chess;

class Round {
	public List<Move> moves = new ArrayList<Move>();
	
	Round() {}

	public void addMove(Move move) { moves.add(move); }
}
