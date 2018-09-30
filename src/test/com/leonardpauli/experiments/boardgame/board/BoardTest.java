package com.leonardpauli.experiments.boardgame.board;

import com.leonardpauli.experiments.boardgame.actor.Home;
import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.board.movement.MovementType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

  @Test
  void tileExistsAt() {}

  @Test
  void tileAt() {}

  @Test
  void removePiece() {}

  @Test
  void getMovementForNotation() throws Exception {
    Board board = new Board();
    Player player = new Player(0);
    Iterator<Home> homes = board.getPlayerHomes();
    player.home = homes.next();

    Player p2 = new Player(1);
    p2.home = homes.next();

    Piece p = new Piece(PieceType.PAWN);
    p.owner = player;
    p.setHome(board.placePiece(p, Position.fromString("c3")));
    List<Movement> as = board.getMovementsForNotation("c", player);
    assertArrayEquals(
        new MovementType[] {MovementType.FORWARD_ONE, MovementType.FORWARD_TWO_FROM_HOME},
        as.stream().map(m -> m.type).toArray());

    p = new Piece(PieceType.QUEEN);
    p.owner = p2;
    p.setHome(board.placePiece(p, Position.fromString("a3")));

    p = new Piece(PieceType.QUEEN);
    p.owner = p2;
    p.setHome(board.placePiece(p, Position.fromString("d4")));

    as = board.getMovementsForNotation("Qc", p2);
    board.setTileMarksFromAvailableMovements(as.toArray(new Movement[] {}));
    assertArrayEquals(
        new MovementType[] {
          MovementType.STRAIGHT,
          MovementType.DIAGONAL,
          MovementType.DIAGONAL,
          MovementType.STRAIGHT,
          MovementType.DIAGONAL,
          MovementType.DIAGONAL
        },
        as.stream().map(m -> m.type).toArray());

    // TODO: assertThrows(InvalidMoveException.class, () -> board.getMovementForNotation("Qa2",
    // player));

    // castling

    for (int i = 0; i < 5; i++) {
      p = new Piece(PieceType.PAWN);
      p.owner = p2;
      p.setHome(board.placePiece(p, new Position(i, 6)));
    }

    p = new Piece(PieceType.ROOK);
    p.owner = p2;
    p.setHome(board.placePiece(p, Position.fromString("a8")));

    p = new Piece(PieceType.KING);
    p.owner = p2;
    p.setHome(board.placePiece(p, Position.fromString("d8")));

    System.out.println(board.toString());
    as = board.getMovementsForNotation("O-O", p2);
    assertEquals(1, as.size());
  }

  @Test
  void placePiece() {}
}
