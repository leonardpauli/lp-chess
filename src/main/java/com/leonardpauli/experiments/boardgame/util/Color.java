package com.leonardpauli.experiments.boardgame.util;

public class Color {
  public static final Color black = new Color("black");
  public static final Color white = new Color("white");

  private String name;

  public Color(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  };

  public static Color fromHSL(float hue, float saturation, float lightness) {
    String name =
        String.format("hsl(%f, %f%%, %f%%)", hue * 360, saturation * 100, lightness * 100);
    return new Color(name);
  }

  public javafx.scene.paint.Color getFXColor() {
    return javafx.scene.paint.Color.web(name);
  }
}
