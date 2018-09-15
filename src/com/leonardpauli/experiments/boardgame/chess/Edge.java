package com.leonardpauli.experiments.boardgame.chess;

class Edge {
	Tile source;
	EdgeType type;
	Tile target;
	
	Edge(Tile source, EdgeType type, Tile target) {
		this.source = source;
		this.type = type;
		this.target = target;
	}
}

public enum EdgeType {
	LEFT ("<", [-1, 0], "left"),
	RIGHT (">", [1, 0], "right"),
	UP ("^", [0, 1], "up"),
	DOWN ("v", [0, -1], "down");

	public final String code;
	public final Point direction;
	public final String name;

	private EdgeType(String code, int[2] dir, String name) {
		this.code = code;
		this.direction = new Point(dir[0], dir[1]);
		this.name = name;
	}

	public static EdgeType fromCode(String code) throws ChessError {
		for (EdgeType t : EdgeType.values())
			if (t.code.equals(code)) return t;
		throw ChessError("enum not found");
	}
}
