package com.leonardpauli.experiments.boardgame.board.movement;

import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;
import com.leonardpauli.experiments.boardgame.board.tile.EdgeType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.game.GameException;

import java.util.ArrayList;
import java.util.List;

import static com.leonardpauli.experiments.boardgame.board.movement.MovementType.*;
import static com.leonardpauli.experiments.boardgame.board.tile.EdgeType.*;

public class MovementProcessor {

  private Board board;

  public MovementProcessor(Board board) {
    this.board = board;
  }

  // public

  // TODO: all "available" methods could probably be static
  // 	and doesn't need the reference to Board
  // 	(because navigation through edges)

  public List<Movement> getAvailable(Piece piece) {
    List<Movement> movements = new ArrayList<Movement>();
    for (MovementType type : piece.type.movementTypes) {
      addAvailableTo(movements, type, piece);
    }
    return movements;
  }

  public List<Movement> getAvailable(Piece piece, Position destination) {
    List<Movement> movements = getAvailable(piece);
    movements.removeIf(movement -> !movement.edge.target.position.equals(destination));
    return movements;
  }

  // private

  private static class Options {
    Tile source;
    EdgeType[] path;
    boolean skipOccupiedInBetween = false;
    boolean skipOccupiedInBetweenRepetitions = false;
    int repetitionsMax = 0;
    boolean allowCapture = true;
    boolean requireCapture = false;
    MovementType type;

    static Options[] withMultiDirectionalRepeatablePath(
        EdgeType[] pathIdeal, Piece piece, MovementType type, int repetitionsMax)
        throws GameException {
      int directions = 4;
      EdgeType[][] paths = new EdgeType[directions][pathIdeal.length];
      for (int turns = 0; turns < directions; turns++) {
        paths[turns] = EdgeType.turnedPath(pathIdeal, turns);
      }

      Options[] opts = new Options[paths.length];
      for (int i = 0; i < paths.length; i++) {
        Options opt = new Options();
        opt.source = piece.tile;
        opt.repetitionsMax = repetitionsMax;
        opt.path = paths[i];
        opt.type = type;
        opts[i] = opt;
      }

      return opts;
    }

    static Options[] withMultiDirectionalRepeatablePath(
        EdgeType[] pathIdeal, Piece piece, MovementType type) throws GameException {
      return withMultiDirectionalRepeatablePath(pathIdeal, piece, type, 8);
    }

    @Override
    protected Options clone() {
      Options o = new Options();
      o.source = source;
      o.path = path;
      o.skipOccupiedInBetween = skipOccupiedInBetween;
      o.skipOccupiedInBetweenRepetitions = skipOccupiedInBetweenRepetitions;
      o.repetitionsMax = repetitionsMax;
      o.type = type;
      o.allowCapture = allowCapture;
      o.requireCapture = requireCapture;
      return o;
    }
  }

  private int addAvailableTo(
      List<Movement> movements, Options opt, Tile lastTile, int segmentIndex) {
    Player player = opt.source.getPiece().owner;

    EdgeType[] segments = opt.path;
    EdgeType segmentType = segments[segmentIndex];
    Edge[] edges = lastTile.getEdges(segmentType);

    int addedCount = 0;

    for (Edge edge : edges) {

      boolean isAtDestination = segmentIndex == segments.length - 1;
      boolean isInBetween = !isAtDestination;
      boolean skipOccupiedCheck = !isAtDestination && (isInBetween && opt.skipOccupiedInBetween);
      if (!skipOccupiedCheck && edge.target.isOccupiedBy(player)) continue;

      if (!isAtDestination) {
        addedCount += addAvailableTo(movements, opt, edge.target, segmentIndex + 1);
        continue;
      }

      Edge fullEdge = new Edge(opt.source, edge.target);
      Movement movement = new Movement(opt.type, fullEdge);

      boolean isCapture = edge.target.hasPiece();
      if (isCapture) {
        movement.setCapturedPiece(edge.target.getPiece());
      }

      if (isCapture ? opt.allowCapture : !opt.requireCapture) {
        movements.add(movement);
        addedCount++;
      }

      boolean isLastSegment = segmentIndex == segments.length - 1;
      if (isLastSegment
          && opt.repetitionsMax > 1
          && (opt.skipOccupiedInBetweenRepetitions || !edge.target.hasPiece())) {
        Options o = opt.clone();
        o.repetitionsMax--;
        addedCount += addAvailableTo(movements, o, edge.target, 0);
      }
    }

    return addedCount;
  }

  private int addAvailableTo(List<Movement> movements, Options opt) {
    return addAvailableTo(movements, opt, opt.source, 0);
  }

  private void addAvailableTo(List<Movement> movements, MovementType type, Piece piece) {
    try {
      addAvailableToThrowing(movements, type, piece);
    } catch (GameException e) {
      throw new RuntimeException(e);
    }
  }

  private void addAvailableToThrowing(List<Movement> movements, MovementType type, Piece piece)
      throws GameException {
    Edge forwardEdge = piece.owner.home.getEdgeForward();
    EdgeType forward = forwardEdge.type;

    if (type == FORWARD_ONE) {
      Options opt = new Options();
      opt.type = FORWARD_ONE;
      opt.source = piece.tile;
      opt.path = new EdgeType[] {forward};
      opt.allowCapture = false;
      addAvailableTo(movements, opt);

    } else if (type == FORWARD_TWO_FROM_HOME) {

      if (!piece.isAtHome()) return;

      Options opt = new Options();
      opt.type = FORWARD_TWO_FROM_HOME;
      opt.source = piece.tile;
      opt.path = new EdgeType[] {forward, forward};
      opt.allowCapture = false;
      addAvailableTo(movements, opt);

    } else if (type == ONE_STEP) {
      Options opt = new Options();
      opt.type = ONE_STEP;
      opt.source = piece.tile;

      opt.path = new EdgeType[] {UP};
      addAvailableTo(movements, opt);
      opt.path = new EdgeType[] {DOWN};
      addAvailableTo(movements, opt);
      opt.path = new EdgeType[] {LEFT};
      addAvailableTo(movements, opt);
      opt.path = new EdgeType[] {RIGHT};
      addAvailableTo(movements, opt);

      opt.skipOccupiedInBetween = true;
      opt.path = new EdgeType[] {UP, LEFT};
      addAvailableTo(movements, opt);
      opt.path = new EdgeType[] {UP, RIGHT};
      addAvailableTo(movements, opt);
      opt.path = new EdgeType[] {DOWN, LEFT};
      addAvailableTo(movements, opt);
      opt.path = new EdgeType[] {DOWN, RIGHT};
      addAvailableTo(movements, opt);

    } else if (type == LMOVE) {
      Options opt = new Options();
      opt.type = LMOVE;
      opt.source = piece.tile;
      opt.skipOccupiedInBetween = true;

      EdgeType[] pathIdeal = {UP, UP, LEFT};
      EdgeType[] pathIdealMirrored = {UP, UP, RIGHT};

      for (int turns = 0; turns < 4; turns++) {
        opt.path = EdgeType.turnedPath(pathIdeal, turns);
        addAvailableTo(movements, opt);
        opt.path = EdgeType.turnedPath(pathIdealMirrored, turns);
        addAvailableTo(movements, opt);
      }

    } else if (type == STRAIGHT) {
      for (Options opt :
          Options.withMultiDirectionalRepeatablePath(new EdgeType[] {UP}, piece, STRAIGHT))
        addAvailableTo(movements, opt);

    } else if (type == DIAGONAL) {
      for (Options opt :
          Options.withMultiDirectionalRepeatablePath(new EdgeType[] {UP, LEFT}, piece, DIAGONAL)) {
        opt.skipOccupiedInBetween = true;
        addAvailableTo(movements, opt);
      }

    } else if (type == FORWARD_DIAGONAL_ONE_CAPTURE) {

      Options opt = new Options();
      opt.type = FORWARD_DIAGONAL_ONE_CAPTURE;
      opt.source = piece.tile;
      opt.path = new EdgeType[] {forward, LEFT};
      opt.skipOccupiedInBetween = true;
      opt.requireCapture = true;
      addAvailableTo(movements, opt);

      opt.path = new EdgeType[] {forward, RIGHT};
      addAvailableTo(movements, opt);

    } else if (type == CASTLING) {
      if (!piece.isAtHome()) return;

      for (EdgeType direction : new EdgeType[] {LEFT, RIGHT}) {
        addAvailableCastlingTo(movements, piece, piece.tile, direction, 0, null, null);
      }

    } else if (type == ENPASSANT) {
      // TODO

    } else if (type == PROMOTION) {
      // TODO

    } else {
      throw new GameException("MovementType movements not implemented for type: " + type);
    }
  }

  private void addAvailableCastlingTo(
      List<Movement> movements,
      Piece piece,
      Tile fromTile,
      EdgeType direction,
      int nr,
      Tile kingTarget,
      Tile rookTarget)
      throws GameException {
    Tile[] nextTileAlts = fromTile.getRelative(direction);
    if (nextTileAlts.length == 0) return;

    Piece rook = null;
    boolean blocked = false;
    Tile last = null;
    for (Tile tile : nextTileAlts) {
      last = tile;
      if (!tile.hasPiece()) {
        blocked = !tile.isSafeFor(piece.owner);
        if (blocked) break;
        continue;
      }

      Piece p = tile.getPiece();
      boolean isOwnRook = p.type == PieceType.ROOK && p.owner.equals(piece.owner);
      blocked = !isOwnRook || !p.isAtHome();
      if (blocked) break;
      rook = p;

      CastlingMovement m =
          new CastlingMovement(
              new Edge(piece.tile, kingTarget), new Edge(rook.tile, rookTarget), direction);
      movements.add(m);
    }
    if (blocked) return;
    if (nr == 0) rookTarget = last;
    if (nr == 1) kingTarget = last;
    if (rook == null && nr < 7) {
      addAvailableCastlingTo(movements, piece, last, direction, nr + 1, kingTarget, rookTarget);
    }
  }
}
