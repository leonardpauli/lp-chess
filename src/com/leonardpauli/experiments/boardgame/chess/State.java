package com.leonardpauli.experiments.boardgame.chess;

public enum State {
	Default("Default", ""),
	Check("Check", "escapable immediate threat for target's king by source"),
	Remi("Remi", ""),
	Stalemate("Stalemate", "player unable to move, but isn't in check or checkmate"),
	Checkmate("Checkmate", "");

	String title;
	String description;

	State(String title, String description) {
		this.title = title;
		this.description = description;
	}
}
