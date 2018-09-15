package com.leonardpauli.experiments.boardgame.chess;


public class ChessException extends Exception {
	public ChessException(String msg) { super(msg); }
}

public class InvalidMoveException extends ChessException {
	
	public enum Type {
		DESTINATION_OCCUPIED ("destination tile occupied"),
		DESTINATION_NOT_FOUND ("destination tile not found"),
		OTHER ("other");

		private final String description;

		Type(String description) {this.description = description;}

		public String toString() {return description;}
	}

	public Type type;

	InvalidMoveException(Type type) {
		super("Invalid move; "+type.toString());
		this.type = type;
	}
	InvalidMoveException(String msg) {
		super("Invalid move; "+msg);
		this.type = Type.OTHER;
	}
}
