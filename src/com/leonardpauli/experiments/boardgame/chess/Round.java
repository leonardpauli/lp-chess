package com.leonardpauli.experiments.boardgame.chess;

import java.util.ArrayList;
import java.util.List;

class Round {
	public List<Move> moves = new ArrayList<Move>();
	
	public void addMove(Move move) { moves.add(move); }
}
