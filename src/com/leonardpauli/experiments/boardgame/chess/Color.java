package com.leonardpauli.experiments.boardgame.chess;

class Color {
	public static final black = new Color("black");
	public static final white = new Color("white");

	String name;
	Color(String name) {this.name = name;}

	public static Color fromHSL(float hue, float saturation, float lightness) {
		String name = "hsl("+
			Long.toString(hue)+", "+
			Long.toString(saturation)+", "+
			Long.toString(lightness)+")";
		return new Color(name);
	}
}
