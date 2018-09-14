package com.leonardpauli.experiments.boardgame.chess;


class Round {
	public List<Move> moves = new ArrayList<Move>();
	Round() {}

	public void addMove(Move move) { moves.add(move); }
}

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

class Event {
	public Date start;
	public Date end;

	Event(Date start) { this.start = start; }
	Event() { this.start = new Date(); }

	public void setEnd(Date date) { this.end = date; }

	public long duration() { // in ms
		return end.getTime()-start.getTime();
	}
}
