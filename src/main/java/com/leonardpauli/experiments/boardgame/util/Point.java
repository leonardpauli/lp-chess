package com.leonardpauli.experiments.boardgame.util;

import javafx.geometry.Point2D;

import java.util.Objects;

public class Point {
  public int x;
  public int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point add(Point p2) {
    return new Point(this.x + p2.x, this.y + p2.y);
  }

  public Point sub(Point p2) {
    return new Point(this.x - p2.x, this.y - p2.y);
  }

  public Point mul(int k) {
    return new Point(this.x * k, this.y * k);
  }

  public Point reversed() {
    return new Point(y, x);
  }

  // equals

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Point)) return false;
    Point p = (Point) o;
    return p.x == x && p.y == y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  // conversions

  public Point2D getPoint2D() {
    return new Point2D((double) x, (double) y);
  }
}
