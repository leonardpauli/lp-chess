package com.leonardpauli.experiments.boardgame.chess;


class Round {
	public List<Move> moves = new ArrayList<Move>();
	Round() {}
}

class Move {
	Player player;
	Move(Player player) {this.player = player;}
}
