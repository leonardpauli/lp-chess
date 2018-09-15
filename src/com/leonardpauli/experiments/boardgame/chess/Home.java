package com.leonardpauli.experiments.boardgame.chess;

class Home {
	public final Position position;
	public final Position positionForward;
	public final Position delta;

	Home(Position position, Position positionForward) {
		this.position = position;
		this.positionForward = positionForward;
		this.delta = positionForward.sub(position);
	}
}
