package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


public class ChessGame {
	Player[] players;
	Board board;
	List<Round> rounds = new ArrayList<Round>();
	State state = State.Default;

	static int movesPerRound = 2;
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
		return players[round.moves.size() % players.length];
	}

	void givePlayersHome() {
		players[0].home = new Home(
			new Position(board.size.x/2, 0),
			new Position(board.size.x/2, 1)
		);
		players[1].home = new Home(
			new Position(board.size.x/2+1, board.size.y-1-0),
			new Position(board.size.x/2+1, board.size.y-1-1)
		);
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
	void addPieceToPlayer(Player player, int deltaY, PieceType type, Position p) throws InvalidMoveException {
		board.placePiece(player.addPiece(new Piece(type)), p)
		board.placePiece(player.addPiece(new Piece(PieceType.Pawn)), p.add(0, deltaY))
	}
	public void resetPieces(Player player) throws InvalidMoveException {
		for (Piece p : player.pieces)
			removePiece(p);

		Position h = player.home.position;
		int queenDeltaFromKing = player.home.delta.y;
		int q = queenDeltaFromKing;
		Position qp = h.add(1*q, 0);

		addPieceToPlayer(player, q, PieceType.Rook, h.add(-3*q, 0));
		addPieceToPlayer(player, q, PieceType.Knight, h.add(-2*q, 0));
		addPieceToPlayer(player, q, PieceType.Bishop, h.add(-1*q, 0));
		addPieceToPlayer(player, q, PieceType.King, h);
		addPieceToPlayer(player, q, PieceType.Queen, qp);
		addPieceToPlayer(player, q, PieceType.Bishop, qp.add(1*q, 0));
		addPieceToPlayer(player, q, PieceType.Knight, qp.add(2*q, 0));
		addPieceToPlayer(player, q, PieceType.Rook, qp.add(3*q, 0));
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

		if (round.moves.size() == ChessGame.movesPerRound) {
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
