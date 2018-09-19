package com.leonardpauli.experiments.boardgame.game;

import java.util.ArrayList;
import java.util.List;

public class Round {
	public List<Move> moves = new ArrayList<Move>();
	
	public void addMove(Move move) { moves.add(move); }
}
