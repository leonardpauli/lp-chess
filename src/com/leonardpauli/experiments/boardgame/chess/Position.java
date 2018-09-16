package com.leonardpauli.experiments.boardgame.chess;

class Position extends Point {
	
	Position(int x, int y) {super(x, y);}
	Position(Point p) {super(p.x, p.y);}

	public static Position fromString(String code) throws ChessException {
		if (code.length() != 2) throw new ChessException(
						"code.length has to be 2, was "+Integer.toString(code.length()));
		int x = ((int)code.charAt(0)) - 65;
		int y = Integer.parseInt(code.substring(1,2));
		return new Position(x, y);
	}

	public String toString() { return colString() + rowString(); }
	public String colString() { return Character.toString((char) (x + 65)); }
	public String rowString() { return Integer.toString(y+1); }
}
