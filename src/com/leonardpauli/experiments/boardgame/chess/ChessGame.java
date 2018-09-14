package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


public class ChessGame {
	Player[] players = new Player[2]; // order determines play order
	Board board;
	List<Round> rounds = new ArrayList<Round>();

	public ChessGame() {
		players = [
			new Player("White", Color.white),
			new Player("Black", Color.black),
		];
		
		board = new Board();

		givePlayersHome();
		resetPieces();
	}

	public Round currentRound() {
		return rounds.get(rounds.size()-1);
	}
	public Player getCurrentPlayer() {
		Round round = currentRound();
		return players[round.moves.size() % players.length];
	}

	public List<Piece> getPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		for (Player p : players)
			pieces.addAll(p.pieces);
		return pieces;
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


	void removePiece(Piece piece) {
		piece.owner.pieces.remove(piece);
		piece.owner = null;
		board.removePiece(piece);
	}
	void addPieceToPlayer(Player player, int deltaY, PieceType type, Position p) throws InvalidMoveException {
		board.placePiece(player.addPiece(new Piece(type)), p)
		board.placePiece(player.addPiece(new Piece(Pawn)), p.add(0, deltaY))
	}
	public void resetPieces(Player player) throws InvalidMoveException {
		for (Piece p : player.pieces)
			removePiece(p);

		Position h = player.home.position;
		int queenDeltaFromKing = player.home.delta.y;
		int q = queenDeltaFromKing;
		Position qp = h.add(1*q, 0);

		addPieceToPlayer(player, q, Rook, h.add(-3*q, 0));
		addPieceToPlayer(player, q, Knight, h.add(-2*q, 0));
		addPieceToPlayer(player, q, Bishop, h.add(-1*q, 0));
		addPieceToPlayer(player, q, King, h);
		addPieceToPlayer(player, q, Queen, qp);
		addPieceToPlayer(player, q, Bishop, qp.add(1*q, 0));
		addPieceToPlayer(player, q, Knight, qp.add(2*q, 0));
		addPieceToPlayer(player, q, Rook, qp.add(3*q, 0));
	}

	public void resetPieces() {
		for (Player p : players)
			resetPieces(player);
	}
}
