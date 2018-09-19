package com.leonardpauli.experiments.boardgame.board.tile;

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

