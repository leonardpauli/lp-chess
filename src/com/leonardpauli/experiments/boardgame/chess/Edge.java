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
	Edge(Tile source, Tile target) {
		this.source = source;
		this.type = EdgeType.ANY;
		this.target = target;
	}
}

public enum EdgeType {
	LEFT ('<', {-1, 0}, "left"),
	RIGHT ('>', {1, 0}, "right"),
	UP ('^', {0, 1}, "up"),
	DOWN ('v', {0, -1}, "down"),
	ANY ('.', {0, 0}, "any");

	public final char code;
	public final Point direction;
	public final String title;
	private final EdgeType turned;

	private EdgeType(char code, int[2] dir, String title) {
		this.code = code;
		this.direction = new Point(dir[0], dir[1]);
		this.title = title;
	}


	// turning

	static {
		LEFT.turned = DOWN;
		DOWN.turned = RIGHT;
		RIGHT.turned = UP;
		UP.turned = LEFT;
	}

	public bool canBeTurned() { return turned != null; }
	public EdgeType getTurned(int times) throws ChessError {
		EdgeType type = this;
		for (int i = 0; i<times; i++) {
			if (!type.canBeTurned())
				throw new ChessError("edge can't be turned");
			type = type.turned;
		}
		return type;
	}
	public EdgeType getTurned() { return getTurned(1); }
	public int getTurns(EdgeType toType) {
		return toType==this? 0: 1 + this.getTurned().getTurns(toType);
	}

	public static EdgeType[] turnedPath(EdgeType[] path, int turns) {
		
	}


	// string

	public static EdgeType fromCode(char code) throws ChessError {
		for (EdgeType t : EdgeType.values())
			if (t.code == code) return t;
		throw ChessError("enum not found");
	}
	public static EdgeType[] getPath(String code) throws ChessError {
		EdgeType[] path = new EdgeType[code.length()];
		int i = 0;
		for (char ch : code.toCharArray())
			path[i++] = fromCode(ch);
		return path;
	}
}
