package com.leonardpauli.experiments.boardgame.game;

import com.leonardpauli.experiments.boardgame.Event;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.Player;

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
