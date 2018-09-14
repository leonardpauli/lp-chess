package com.leonardpauli.experiments.boardgame.chess;

class Point {
	public int x;
	public int y;
	Point(int x, int y) {this.x = x; this.y = y;}
	public Point sub(Point p2) { return new Point(this.x+p2.x, this.y+p2.y); }
	public Point sub(Point p2) { return new Point(this.x-p2.x, this.y-p2.y); }
	public Point mul(int k) { return new Point(this.x*k, this.y*k); }
	public Point reversed() {return new Point(y, x); }
}

class Size extends Point {Size(int x, int y) {super(x, y)}}
