package com.leonardpauli.experiments.boardgame.chess;

class Color {
	public static final Color black = new Color("black");
	public static final Color white = new Color("white");

	private String name;
	Color(String name) { this.name = name; }

	public String getName() { return name; };

	public static Color fromHSL(float hue, float saturation, float lightness) {
		String name = "hsl("+
			Float.toString(hue)+", "+
			Float.toString(saturation)+", "+
			Float.toString(lightness)+")";
		return new Color(name);
	}
}
