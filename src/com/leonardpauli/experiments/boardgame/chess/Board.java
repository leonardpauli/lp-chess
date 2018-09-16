package com.leonardpauli.experiments.boardgame.chess;


class Board {
	public Tile[][] tiles;
	public final Size size = new Size(8, 8);

	Board() {
		setupTiles();
		setupTileEdges();
	}


	// tile

	private void setupTiles() {
		tiles = new Tiles[size.x][size.y];
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Position position = new Position(x, y);
				Tile tile = tiles[x][y] = new Tile(position);
			}
		}
	}
	private void setupTileEdges() {
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Position position = new Position(x, y);
				Tile tile = tiles[x][y];
				tile.setEdges(getTileEdges(tile));
			}
		}
	}

	public bool tileExistsAt(Position position) {
		bool inBounds =
			0 <= position.x && position.x < tiles.length &&
			0 <= position.y && position.y < tiles[position.x].length;
		return inBounds;
	}

	public Tile getTileAt(Position position) throws InvalidMoveException {
		if (!tileExistsAt(position)) throw InvalidMoveException(
			InvalidMoveException.Type.DESTINATION_NOT_FOUND);

		return tiles[position.x][position.y];
	}

	private Edge[] getTileEdges(Tile tile) {
		int maxSize = EdgeType.values().length;
		Edge[] edges = new Edge[maxSize];
		int i = 0;
		for (EdgeType type : EdgeType.values()) {
			if (tileExistsAt(type.direction))
				edges[i++] = new Edge(tile, type, getTileAt(position));
		}
		int actualSize = i;
		return Arrays.copyOf(edges, actualSize);
	}


	// piece

	public void removePiece(Piece piece) {
		if (piece.position)
			tiles[piece.position.x][piece.position.y].piece = null;
		piece.position = null;
	}

	public Tile placePiece(Piece piece, Tile tile) throws InvalidMoveException {
		if (tile.piece!=null) throw InvalidMoveException(
			InvalidMoveException.Type.DESTINATION_OCCUPIED);

		piece.position = position;
		tile.piece = piece;

		return tile;
	}
	public Tile placePiece(Piece piece, Position position) throws InvalidMoveException {
		Tile tile = getTileAt(position);
		return placePiece(piece, tile);
	}

	public Piece getPieceAt(Position position) throws InvalidMoveException {
		return getTileAt(position).piece;
	}
	public Piece getPieceAt(String code) throws ChessException, InvalidMoveException {
		return getPieceAt(new Position(code));
	}
	

	// movement

	private class MovementDescription {
		public Tile source;
		public EdgeType[] path;
		public bool skipOccupiedInBetween = false;
	}
	private void addAvaliableMovementsTo(List<Movement> movements, MovementDescription opt,
		Tile lastTile, int segmentIndex) {

		EdgeType[] segments = opt.path;
		EdgeType segmentType = segments[segmentIndex];
		Edge[] edges = lastTile.getEdges(segmentType);

		for (Edge edge : edges) {

			bool isAtDestination = segmentIndex == segmentTypes.length-1;
			bool isInBetween = !isAtDestination;
			bool skipOccupiedCheck = !isAtDestination && (isInBetween && opt.skipOccupiedInBetween);
			if (!skipOccupiedCheck && edge.target.isOccupiedBy(player)) continue;

			if (!isAtDestination) {
				addAvaliableMovementsTo(movements, opt, edge.target, segmentIndex+1);
				continue;
			}

			Edge fullEdge = new Edge(opt.source, edge.target);
			Movement movement = new Movement(type, fullEdge);
			movements.add(movement);
		}
	}
	private void addAvaliableMovementsTo(List<Movement> movements, MovementDescription opt) {
		return addAvaliableMovementsTo(movements, opt, opt.source, 0);
	}

	private void addAvaliableMovementsTo(List<Movement> movements, MovementType type, Piece piece) {
		Edge forwardEdge = piece.owner.getHomeEdgeForward();
		EdgeType forward = forwardEdge.type;
		Player player = piece.owner;

		if (type == FORWARD_ONE) {
			MovementDescription opt = new MovementDescription();
			opt.source = piece.tile;
			opt.path = {forward};
			addAvaliableMovementsTo(movements, opt);

		} else if (type == FORWARD_TWO_FROM_HOME) {

			if (!piece.isAtHome()) return;

			MovementDescription opt = new MovementDescription();
			opt.source = piece.tile;
			opt.path = {forward, forward};
			addAvaliableMovementsTo(movements, opt);

		} else if (type == ONE_STEP) {
			MovementDescription opt = new MovementDescription();
			opt.source = piece.tile;
			opt.path = {EdgeType.UP}; addAvaliableMovementsTo(movements, opt);
			opt.path = {EdgeType.DOWN}; addAvaliableMovementsTo(movements, opt);
			opt.path = {EdgeType.LEFT}; addAvaliableMovementsTo(movements, opt);
			opt.path = {EdgeType.RIGHT}; addAvaliableMovementsTo(movements, opt);
			opt.skipOccupiedInBetween = true
			opt.path = {EdgeType.UP, EdgeType.LEFT}; addAvaliableMovementsTo(movements, opt);
			opt.path = {EdgeType.UP, EdgeType.RIGHT}; addAvaliableMovementsTo(movements, opt);
			opt.path = {EdgeType.DOWN, EdgeType.LEFT}; addAvaliableMovementsTo(movements, opt);
			opt.path = {EdgeType.DOWN, EdgeType.RIGHT}; addAvaliableMovementsTo(movements, opt);

		} else if (type == LMOVE) {
			MovementDescription opt = new MovementDescription();
			opt.source = piece.tile;
			opt.skipOccupiedInBetween = true

			EdgeType[] pathIdeal = {EdgeType.UP, EdgeType.UP, EdgeType.LEFT};
			EdgeType[] pathIdealMirrored = {EdgeType.UP, EdgeType.UP, EdgeType.RIGHT};

			for (int turns = 0; i<4; i++) {
				opt.path = EdgeType.turnedPath(pathIdeal, turns); addAvaliableMovementsTo(movements, opt);
				opt.path = EdgeType.turnedPath(pathIdealMirrored, turns); addAvaliableMovementsTo(movements, opt);
			}

		} else {
			// TODO
			throw new ChessException("not implemented");
		}
	}

	public List<Movement> getAvailableMovements(Piece piece) {
		List<Movement> movements = new ArrayList<Movement>();
		for (MovementType type : piece.type.movementTypes) {
			addAvaliableMovementsTo(movements, type, piece);
		}
		return movements;
	}

	public List<Movement> getAvailableMovements(Piece piece, Position destination) {
		List<Movement> movements = getAvailableMovements(piece);
		movements.removeIf(movement -> !movement.edge.target.position.equals(destination));
		return movements;
	}


	// utils

	public String toString(Style style) { return Printer.boardToString(this, style); }
	public String toString() { return toString(Printer.Style.PRETTY); }
}
