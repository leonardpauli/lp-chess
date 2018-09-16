package com.leonardpauli.experiments.boardgame.chess;

class BoardMovementProcessor {

	private Board board;

	BoardMovementProcessor(Board board) {this.board = board;}


	// public

	public List<Movement> getAvailable(Piece piece) {
		List<Movement> movements = new ArrayList<Movement>();
		for (MovementType type : piece.type.movementTypes) {
			addAvaliableTo(movements, type, piece);
		}
		return movements;
	}

	public List<Movement> getAvailable(Piece piece, Position destination) {
		List<Movement> movements = getAvailable(piece);
		movements.removeIf(movement -> !movement.edge.target.position.equals(destination));
		return movements;
	}


	// private

	private class Options {
		public Tile source;
		public EdgeType[] path;
		public bool skipOccupiedInBetween = false;
		public bool repeatable = false;

		public static Options[] withMultiDirectionalRepeatablePath(EdgeType[] pathIdeal, Piece piece) {
			int directions = 4;
			EdgeType[][] paths = new EdgeType[directions][pathIdeal.length];
			for (int turns = 0; turns<directions; turns++) {
				paths[turns] = EdgeType.turnedPath(pathIdeal, turns);
			}

			Options[] opts = new Options[paths.length]
			for (int i = 0; i<paths.length; i++) {
				Options opt = new Options();
				opt.source = piece.tile;
				opt.repeatable = true;
				opt.path = paths[i];
				opts[i] = opt;
			}

			return opts;
		}
	}
	private int addAvaliableTo(List<Movement> movements, Options opt,
		Tile lastTile, int segmentIndex) {

		EdgeType[] segments = opt.path;
		EdgeType segmentType = segments[segmentIndex];
		Edge[] edges = lastTile.getEdges(segmentType);

		int addedCount = 0;

		for (Edge edge : edges) {

			bool isAtDestination = segmentIndex == segmentTypes.length-1;
			bool isInBetween = !isAtDestination;
			bool skipOccupiedCheck = !isAtDestination && (isInBetween && opt.skipOccupiedInBetween);
			if (!skipOccupiedCheck && edge.target.isOccupiedBy(player)) continue;

			if (!isAtDestination) {
				addedCount += addAvaliableTo(movements, opt, edge.target, segmentIndex+1);
				continue;
			}

			Edge fullEdge = new Edge(opt.source, edge.target);
			Movement movement = new Movement(type, fullEdge);
			movements.add(movement);
			addedCount++;
		}

		return addedCount;
	}
	private int addAvaliableTo(List<Movement> movements, Options opt) {
		return addAvaliableTo(movements, opt, opt.source, 0);
	}

	private void addAvaliableTo(List<Movement> movements, MovementType type, Piece piece) {
		Edge forwardEdge = piece.owner.getHomeEdgeForward();
		EdgeType forward = forwardEdge.type;
		Player player = piece.owner;

		if (type == FORWARD_ONE) {
			Options opt = new Options();
			opt.source = piece.tile;
			opt.path = {forward};
			addAvaliableTo(movements, opt);

		} else if (type == FORWARD_TWO_FROM_HOME) {

			if (!piece.isAtHome()) return;

			Options opt = new Options();
			opt.source = piece.tile;
			opt.path = {forward, forward};
			addAvaliableTo(movements, opt);

		} else if (type == ONE_STEP) {
			Options opt = new Options();
			opt.source = piece.tile;
			opt.path = {EdgeType.UP}; addAvaliableTo(movements, opt);
			opt.path = {EdgeType.DOWN}; addAvaliableTo(movements, opt);
			opt.path = {EdgeType.LEFT}; addAvaliableTo(movements, opt);
			opt.path = {EdgeType.RIGHT}; addAvaliableTo(movements, opt);
			opt.skipOccupiedInBetween = true
			opt.path = {EdgeType.UP, EdgeType.LEFT}; addAvaliableTo(movements, opt);
			opt.path = {EdgeType.UP, EdgeType.RIGHT}; addAvaliableTo(movements, opt);
			opt.path = {EdgeType.DOWN, EdgeType.LEFT}; addAvaliableTo(movements, opt);
			opt.path = {EdgeType.DOWN, EdgeType.RIGHT}; addAvaliableTo(movements, opt);

		} else if (type == LMOVE) {
			Options opt = new Options();
			opt.source = piece.tile;
			opt.skipOccupiedInBetween = true;

			EdgeType[] pathIdeal = {EdgeType.UP, EdgeType.UP, EdgeType.LEFT};
			EdgeType[] pathIdealMirrored = {EdgeType.UP, EdgeType.UP, EdgeType.RIGHT};

			for (int turns = 0; turns<4; turns++) {
				opt.path = EdgeType.turnedPath(pathIdeal, turns); addAvaliableTo(movements, opt);
				opt.path = EdgeType.turnedPath(pathIdealMirrored, turns); addAvaliableTo(movements, opt);
			}

		} else if (type == STRAIGHT) {
			for (Options opt : Options
				.withMultiDirectionalRepeatablePath({EdgeType.UP}, piece))
				addAvaliableTo(movements, opt);

		} else if (type == DIAGONAL) {
			for (Options opt : Options
				.withMultiDirectionalRepeatablePath({EdgeType.UP, EdgeType.LEFT}, piece))
				addAvaliableTo(movements, opt);

		} else {
			// TODO: CASTLING, ENPASSANT, PROMOTION
			throw new ChessException("not implemented");
		}
	}

}
