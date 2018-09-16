package com.leonardpauli.experiments.boardgame.chess;

import java.util.ArrayList;
import java.util.List;

import static com.leonardpauli.experiments.boardgame.chess.EdgeType.*;
import static com.leonardpauli.experiments.boardgame.chess.MovementType.*;

class BoardMovementProcessor {

	private Board board;

	BoardMovementProcessor(Board board) {this.board = board;}


	// public

	// TODO: all "available" methods could probably be static
	// 	and doesn't need the reference to Board
	// 	(because navigation through edges)

	public List<Movement> getAvailable(Piece piece) throws ChessException {
		List<Movement> movements = new ArrayList<Movement>();
		for (MovementType type : piece.type.movementTypes) {
			addAvailableTo(movements, type, piece);
		}
		return movements;
	}

	public List<Movement> getAvailable(Piece piece, Position destination) throws ChessException {
		List<Movement> movements = getAvailable(piece);
		movements.removeIf(movement -> !movement.edge.target.position.equals(destination));
		return movements;
	}


	// private

	private static class Options {
		Tile source;
		EdgeType[] path;
		boolean skipOccupiedInBetween = false;
		boolean repeatable = false;
		MovementType type;

		static Options[] withMultiDirectionalRepeatablePath(EdgeType[] pathIdeal, Piece piece, MovementType type) throws ChessException {
			int directions = 4;
			EdgeType[][] paths = new EdgeType[directions][pathIdeal.length];
			for (int turns = 0; turns<directions; turns++) {
				paths[turns] = EdgeType.turnedPath(pathIdeal, turns);
			}

			Options[] opts = new Options[paths.length];
			for (int i = 0; i<paths.length; i++) {
				Options opt = new Options();
				opt.source = piece.tile;
				opt.repeatable = true;
				opt.path = paths[i];
				opt.type = type;
				opts[i] = opt;
			}

			return opts;
		}
	}
	private int addAvailableTo(List<Movement> movements, Options opt,
		Tile lastTile, int segmentIndex) {
		Player player = opt.source.getPiece().owner;

		EdgeType[] segments = opt.path;
		EdgeType segmentType = segments[segmentIndex];
		Edge[] edges = lastTile.getEdges(segmentType);

		int addedCount = 0;

		for (Edge edge : edges) {

			boolean isAtDestination = segmentIndex == segments.length-1;
			boolean isInBetween = !isAtDestination;
			boolean skipOccupiedCheck = !isAtDestination && (isInBetween && opt.skipOccupiedInBetween);
			if (!skipOccupiedCheck && edge.target.isOccupiedBy(player)) continue;

			if (!isAtDestination) {
				addedCount += addAvailableTo(movements, opt, edge.target, segmentIndex+1);
				continue;
			}

			Edge fullEdge = new Edge(opt.source, edge.target);
			Movement movement = new Movement(opt.type, fullEdge);
			movements.add(movement);
			addedCount++;
		}

		return addedCount;
	}
	private int addAvailableTo(List<Movement> movements, Options opt) {
		return addAvailableTo(movements, opt, opt.source, 0);
	}

	private void addAvailableTo(List<Movement> movements, MovementType type, Piece piece) throws ChessException {
		Edge forwardEdge = piece.owner.home.getEdgeForward();
		EdgeType forward = forwardEdge.type;

		if (type == FORWARD_ONE) {
			Options opt = new Options();
			opt.type = FORWARD_ONE;
			opt.source = piece.tile;
			opt.path = new EdgeType[]{forward};
			addAvailableTo(movements, opt);

		} else if (type == FORWARD_TWO_FROM_HOME) {

			if (!piece.isAtHome()) return;

			Options opt = new Options();
			opt.type = FORWARD_TWO_FROM_HOME;
			opt.source = piece.tile;
			opt.path = new EdgeType[]{forward, forward};
			addAvailableTo(movements, opt);

		} else if (type == ONE_STEP) {
			Options opt = new Options();
			opt.type = ONE_STEP;
			opt.source = piece.tile;

			opt.path = new EdgeType[]{UP}; addAvailableTo(movements, opt);
			opt.path = new EdgeType[]{DOWN}; addAvailableTo(movements, opt);
			opt.path = new EdgeType[]{LEFT}; addAvailableTo(movements, opt);
			opt.path = new EdgeType[]{RIGHT}; addAvailableTo(movements, opt);

			opt.skipOccupiedInBetween = true;
			opt.path = new EdgeType[]{UP, LEFT}; addAvailableTo(movements, opt);
			opt.path = new EdgeType[]{UP, RIGHT}; addAvailableTo(movements, opt);
			opt.path = new EdgeType[]{DOWN, LEFT}; addAvailableTo(movements, opt);
			opt.path = new EdgeType[]{DOWN, RIGHT}; addAvailableTo(movements, opt);

		} else if (type == LMOVE) {
			Options opt = new Options();
			opt.type = LMOVE;
			opt.source = piece.tile;
			opt.skipOccupiedInBetween = true;

			EdgeType[] pathIdeal = {UP, UP, LEFT};
			EdgeType[] pathIdealMirrored = {UP, UP, RIGHT};

			for (int turns = 0; turns<4; turns++) {
				opt.path = EdgeType.turnedPath(pathIdeal, turns); addAvailableTo(movements, opt);
				opt.path = EdgeType.turnedPath(pathIdealMirrored, turns); addAvailableTo(movements, opt);
			}

		} else if (type == STRAIGHT) {
			for (Options opt : Options
				.withMultiDirectionalRepeatablePath(new EdgeType[]{UP}, piece, STRAIGHT))
				addAvailableTo(movements, opt);

		} else if (type == DIAGONAL) {
			for (Options opt : Options
				.withMultiDirectionalRepeatablePath(new EdgeType[]{UP, LEFT}, piece, DIAGONAL))
				addAvailableTo(movements, opt);

		} else if (type == CASTLING) {
			// TODO

		} else if (type == ENPASSANT) {
			// TODO

		} else if (type == PROMOTION) {
			// TODO

		} else {
			throw new ChessException("MovementType movements not implemented for type: "+type);
		}
	}

}
