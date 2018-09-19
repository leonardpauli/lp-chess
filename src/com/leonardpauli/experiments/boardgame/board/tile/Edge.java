package com.leonardpauli.experiments.boardgame.board.tile;

public class Edge {
	public Tile source;
	public EdgeType type;
	public Tile target;
	
	public Edge(Tile source, EdgeType type, Tile target) {
		this.source = source;
		this.type = type;
		this.target = target;
	}
	public Edge(Tile source, Tile target) {
		this.source = source;
		this.type = EdgeType.ANY;
		this.target = target;
	}
}

