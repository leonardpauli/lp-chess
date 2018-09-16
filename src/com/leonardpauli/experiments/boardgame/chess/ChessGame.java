package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


public class ChessGame {
	Player[] players;
	Board board;
	List<Round> rounds = new ArrayList<Round>();
	State state = State.Default;

	static int movesPerPlayerAndRound = 1;
	static int playersCount = 2;


	public ChessGame() {
		createPlayers()
		board = new Board();
		givePlayersHome();
		resetPieces();
		rounds.add(new Round());
	}


	// round

	public Round currentRound() {
		return rounds.get(rounds.size()-1);
	}


	// player

	void createPlayers() {
		players = new Player[ChessGame.playersCount];
		for (int i = 0; i<players.length; i++)
			players[i] = new Player(i);
	}

	public Player getCurrentPlayer() {
		Round round = currentRound();
		int turnNr = round.moves.size() / ChessGame.movesPerPlayerAndRound;
		int turnNrSafe = turnNr % players.length;
		return players[turnNrSafe];
	}

	void givePlayersHome() {
		players[0].setHome(new Home(
			new Position(board.size.x/2, 0),
			new Position(board.size.x/2, 1),
			board
		));
		players[1].setHome(new Home(
			new Position(board.size.x/2+1, board.size.y-1-0),
			new Position(board.size.x/2+1, board.size.y-1-1),
			board
		));
	}


	// piece

	public List<Piece> getPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		for (Player p : players)
			pieces.addAll(p.pieces);
		return pieces;
	}

	void removePiece(Piece piece) {
		piece.owner.pieces.remove(piece);
		piece.owner = null;
		board.removePiece(piece);
	}
	void addPieceToPlayer(Player player, PieceType type, String pathFromHome) throws InvalidMoveException {
		int turns = player.home.getAngleInTurns();
		EdgeType[] pathIdeal = EdgeType.getPath(pathFromHome);
		EdgeType[] path = EdgeType.turnedPath(pathIdeal, turns);

		Tile tile = player.home.getTile().getFirstRelative(path);
		EdgeType forward = player.home.getEdgeForward().type;
		Position pawnTile = tile.getRelative(forward)[0];

		Piece pawn = new Piece(PAWN)
		Piece piece = new Piece(type);

		piece.setHome(board.placePiece(player.addPiece(piece), tile));
		pawn.setHome(board.placePiece(player.addPiece(pawn), pawnTile));
	}
	public void resetPieces(Player player) throws InvalidMoveException {
		for (Piece p : player.pieces)
			removePiece(p);

		addPieceToPlayer(player, ROOK, "<<<");
		addPieceToPlayer(player, KNIGHT, "<<");
		addPieceToPlayer(player, BISHOP, "<");
		addPieceToPlayer(player, KING, "");
		addPieceToPlayer(player, QUEEN, ">");
		addPieceToPlayer(player, BISHOP, ">>");
		addPieceToPlayer(player, KNIGHT, ">>>");
		addPieceToPlayer(player, ROOK, ">>>>");
	}

	public void resetPieces() {
		for (Player p : players)
			resetPieces(player);
	}


	// move

	void validateMove(Move move) throws InvalidMoveException {
		// TODO
		if (move.player != getCurrentPlayer())
			throw InvalidMoveException("not that players turn");

		// TODO if state != State.Default ...
	}
	public void playMove(Move move) throws InvalidMoveException {
		validateMove(move);

		Round round = currentRound();
		round.addMove(move);
		
		// TODO: check times, etc

		refreshState();

		if (round.moves.size() == ChessGame.movesPerPlayerAndRound * players.length) {
			rounds.add(new Round());
		}
	}


	// state

	public void refreshState() {
		// TODO: check if check mate, etc
		// state = ...
	}

	public State getState() { return state; }
}
