package com.leonardpauli.experiments.boardgame.actor;

import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;
import com.leonardpauli.experiments.boardgame.board.tile.EdgeType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.game.GameException;

class Home {
	private Position position;
	private Position positionForward;
	private Position delta;
	private Edge edgeForward;

	Home(Position position, Position positionForward, Board board) throws GameException {
		setPositions(position, positionForward);
		updateEdgeForward(board);
	}
	Home(Edge edgeForward) {
		this.edgeForward = edgeForward;
		setPositions(edgeForward.source.position, edgeForward.target.position);
	}

	void setPositions(Position position, Position positionForward) {
		this.position = position;
		this.positionForward = positionForward;
		this.delta = new Position(positionForward.sub(position));
	}

	void updateEdgeForward(Board board) throws GameException {
		Tile homeTile = board.tileAt(position);
		Tile forwardTile = board.tileAt(positionForward);
		for (Edge edge : homeTile.getEdges()) {
			if (edge.target.equals(forwardTile)) {
				this.edgeForward = edge;
				return;
			}
		}
		throw new GameException("Player.setHome: edge from homeTile to forwardTile not found");
	}


	public Position getPosition () { return this.position; }
	public Position getDelta () { return this.delta; }
	public Edge getEdgeForward() { return edgeForward; }
	public Tile getTile() { return edgeForward.source; }
	
	public int getAngleInTurns () throws GameException {
		return getEdgeForward().type.getTurns(EdgeType.UP);
	}

	public int getRank(Position pos) {
		return delta.y * (pos.y - position.y);
	}
}
