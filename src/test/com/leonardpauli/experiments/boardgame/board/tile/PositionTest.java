package com.leonardpauli.experiments.boardgame.board.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

  @Test
  void fromString() throws Exception {
    Position pos = Position.fromString("B4");
    assertEquals(1, pos.x);
    assertEquals(3, pos.y);
    pos = Position.fromString("a1");
    assertEquals(0, pos.x);
    assertEquals(0, pos.y);
  }

  @Test
  void toStringTest() throws Exception {
    assertEquals("C7", Position.fromString("C7").toString());
  }
}
