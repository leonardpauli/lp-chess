package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


class Player {
	String name;
	Color color;

	private Home home;
	private Edge homeEdgeForward;
	
	bool alive = true;
	List<Piece> pieces = new ArrayList<Piece>();

	Player(String name, Color color) {
		this.name = name; this.color = color;
	}
	Player(int ordinal) {
		this.color =
				ordinal == 0 ? Color.white
			: ordinal == 1 ? Color.black
			: Color.fromHSL((float) ordinal / 10, 0.5, 0.5);
		this.name = 
				ordinal == 0 ? "White"
			: ordinal == 1 ? "Black"
			: "Player " + Integer.toString(1 + ordinal);
	}


	// home

	public void setHome(Edge homeEdgeForward) {
		this.homeEdgeForward = homeEdgeForward;
		this.home = new Home(
			homeEdgeForward.source.position,
			homeEdgeForward.target.position,
		);
	}

	public void setHome(Home home, Board board) {
		this.home = home;
		Tile homeTile = board.tileAt(home.position);
		Tile forwardTile = board.tileAt(home.positionForward);
		for (Edge edge : homeTile.getEdges()) {
			if (edge.target.equals(forwardTile)) {
				this.homeEdgeForward = edge;
				return;
			}
		}
		throw new ChessException("Player.setHome: edge from homeTile to forwardTile not found");
	}

	public Home getHome() { return home; }
	public Edge getHomeEdgeForward() { return homeEdgeForward; }


	// piece

	public Piece addPiece(Piece piece) {
		pieces.add(piece);
		piece.owner = this;
		return Piece;
	}

}
