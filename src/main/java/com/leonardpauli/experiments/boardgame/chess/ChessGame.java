package com.leonardpauli.experiments.boardgame.chess;

import com.leonardpauli.experiments.boardgame.actor.Home;
import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.movement.CastlingMovement;
import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.tile.EdgeType;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.game.Move;
import com.leonardpauli.experiments.boardgame.game.Round;
import com.leonardpauli.experiments.boardgame.game.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChessGame {
  private Player[] players;
  public final Board board;
  private List<Round> rounds = new ArrayList<Round>();
  private State state = State.DEFAULT;

  private static int movesPerPlayerAndRound = 1;
  private static int playersCount = 2;

  public ChessGame() throws Exception {
    createPlayers();
    board = new Board();
    givePlayersHome();
    resetPieces();
    rounds.add(new Round());
  }

  // round

  public Round currentRound() {
    return rounds.get(rounds.size() - 1);
  }

  // player

  private void createPlayers() {
    players = new Player[ChessGame.playersCount];
    for (int i = 0; i < players.length; i++) players[i] = new Player(i);
  }

  public Player[] getPlayers() {
    return players;
  }

  public Player getCurrentPlayer() {
    Round round = currentRound();
    int turnNr = round.moves.size() / ChessGame.movesPerPlayerAndRound;
    int turnNrSafe = turnNr % players.length;
    return players[turnNrSafe];
  }

  private void givePlayersHome() {
    Iterator<Home> homes = board.getPlayerHomes();
    for (Player player : players) player.home = homes.next();
  }

  // piece

  public List<Piece> getPieces() {
    List<Piece> pieces = new ArrayList<Piece>();
    for (Player p : players) pieces.addAll(p.pieces);
    return pieces;
  }

  private void removePiece(Piece piece) {
    piece.owner.pieces.remove(piece);
    piece.owner = null;
    board.removePiece(piece);
  }

  private void addPieceToPlayer(Player player, PieceType type, String pathFromHome)
      throws GameException {
    int turns = player.home.getAngleInTurns();
    EdgeType[] pathIdeal = EdgeType.getPath(pathFromHome);
    EdgeType[] path = pathIdeal; // EdgeType.turnedPath(pathIdeal, turns);

    Tile tile = player.home.getTile().getFirstRelative(path);
    EdgeType forward = player.home.getEdgeForward().type;
    Tile pawnTile = tile.getRelative(forward)[0];

    Piece pawn = new Piece(PieceType.PAWN);
    Piece piece = new Piece(type);

    piece.setHome(board.placePiece(player.addPiece(piece), tile));
    pawn.setHome(board.placePiece(player.addPiece(pawn), pawnTile));
  }

  private void resetPieces(Player player) throws GameException {
    for (Piece p : player.pieces) removePiece(p);

    addPieceToPlayer(player, PieceType.ROOK, "<<<<");
    addPieceToPlayer(player, PieceType.KNIGHT, "<<<");
    addPieceToPlayer(player, PieceType.BISHOP, "<<");
    addPieceToPlayer(player, PieceType.QUEEN, "<");
    addPieceToPlayer(player, PieceType.KING, "");
    addPieceToPlayer(player, PieceType.BISHOP, ">");
    addPieceToPlayer(player, PieceType.KNIGHT, ">>");
    addPieceToPlayer(player, PieceType.ROOK, ">>>");
  }

  private void resetPieces() throws GameException {
    for (Player player : players) resetPieces(player);
  }

  // move

  public void validateMove(Move move) throws InvalidMoveException {
    // TODO
    if (!move.player.equals(getCurrentPlayer())
        || !move.movement.edge.source.getPiece().owner.equals(getCurrentPlayer()))
      throw new InvalidMoveException("not that players turn");

    // TODO if state != State.DEFAULT ...
  }

  public void playMove(Move move) throws InvalidMoveException {
    validateMove(move);

    Round round = currentRound();
    round.addMove(move);

    Tile target = move.movement.edge.target;
    boolean shouldCapture = target.hasPiece();
    if (shouldCapture) {
      move.movement.setCapturedPiece(target.getPiece());
      target.getPiece().tile.removePiece();
    }

    target.setPiece(move.piece);

    if (move.movement instanceof CastlingMovement) {
      CastlingMovement cm = (CastlingMovement) move.movement;
      cm.rookEdge.target.setPiece(cm.rookEdge.source.getPiece());
    }

    // TODO: check times, etc

    refreshState();

    if (round.moves.size() == ChessGame.movesPerPlayerAndRound * players.length) {
      rounds.add(new Round());
    }
  }

  // state

  private void refreshState() {
    // TODO: check if check mate, etc
    // state = ...
  }

  public State getState() {
    return state;
  }

  // string

  public String getNotationString() {
    // TODO: utilize own notation package instead (eg. for castling, etc)

    StringBuilder sb = new StringBuilder();
    int i = 0;
    for (Round r : rounds) {
      sb.append((++i) + ".");
      for (Move m : r.moves) {
        sb.append(" ");
        sb.append(m.piece.type.letter);
        sb.append(m.movement.edge.source.position.toString().toLowerCase());
        if (m.movement.getCapturedPiece().isPresent()) sb.append("x");
        sb.append(m.movement.edge.target.position.toString().toLowerCase());
      }
      sb.append(" ");
    }
    return sb.toString();
  }
}
