package com.leonardpauli.experiments.boardgame.chess;

class Move {
	public Player player;
	public Event event;
	public Movement movement;
	public Piece piece;

	Move(Player player, Piece piece, Movement movement) {
		this.player = player;
		this.event = new Event();
		this.piece = piece;
		this.movement = movement;
	}

	public Move getReverseMove() {
		// TODO: for undo
		return null;
	}
}
