package com.leonardpauli.experiments.boardgame.chess;

public enum EdgeType {
	LEFT ('<', new int[]{-1, 0}, "left"),
	RIGHT ('>', new int[]{1, 0}, "right"),
	UP ('^', new int[]{0, 1}, "up"),
	DOWN ('v', new int[]{0, -1}, "down"),
	ANY ('.', new int[]{0, 0}, "any");

	public final char code;
	public final Point direction;
	public final String title;
	private EdgeType turned;

	private EdgeType(char code, int[] dir, String title) {
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

	public boolean canBeTurned() { return turned != null; }
	public EdgeType getTurned(int times) throws ChessException {
		EdgeType type = this;
		for (int i = 0; i<times; i++) {
			if (!type.canBeTurned())
				throw new ChessException("edge can't be turned");
			type = type.turned;
		}
		return type;
	}
	public EdgeType getTurned() throws ChessException { return getTurned(1); }
	public int getTurns(EdgeType toType) throws ChessException {
		return toType==this? 0: 1 + this.getTurned().getTurns(toType);
	}

	public static EdgeType[] turnedPath(EdgeType[] path, int turns) throws ChessException {
		EdgeType[] result = new EdgeType[path.length];
		for (int i = 0; i<path.length; i++)
			result[i] = path[i].getTurned(turns);
		return result;
	}


	// string

	public static EdgeType fromCode(char code) throws ChessException {
		for (EdgeType t : EdgeType.values())
			if (t.code == code) return t;
		throw new ChessException("enum not found");
	}
	public static EdgeType[] getPath(String code) throws ChessException {
		EdgeType[] path = new EdgeType[code.length()];
		int i = 0;
		for (char ch : code.toCharArray())
			path[i++] = fromCode(ch);
		return path;
	}
	public static String stringFromPath(EdgeType[] path) {
		StringBuilder sb = new StringBuilder();
		for (EdgeType type : path) sb.append(String.valueOf(type.code));
		return sb.toString();
	}

}
