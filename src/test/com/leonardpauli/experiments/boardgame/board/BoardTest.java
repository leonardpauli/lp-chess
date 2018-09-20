package com.leonardpauli.experiments.boardgame.board;

import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardTest {

  @Test
  void tileExistsAt() {}

  @Test
  void tileAt() {}

  @Test
  void removePiece() {}

  @Test
  void getMovementForNotation() throws Exception {
    Player player = new Player(0);
    Board board = new Board();
    assertThrows(InvalidMoveException.class, () -> board.getMovementForNotation("Qa2", player));
  }

  @Test
  void placePiece() {}
}
