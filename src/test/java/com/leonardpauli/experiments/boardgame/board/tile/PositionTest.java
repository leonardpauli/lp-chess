package com.leonardpauli.experiments.boardgame.board.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PositionTest {

  @Test
  public void testFromString() throws Exception {
    Position pos = Position.fromString("B4");
    assertEquals(1, pos.x);
    assertEquals(3, pos.y);
    pos = Position.fromString("a1");
    assertEquals(0, pos.x);
    assertEquals(0, pos.y);

    assertThrows(Exception.class, () -> Position.fromString(""));
    assertThrows(Exception.class, () -> Position.fromString("a"));
    assertThrows(Exception.class, () -> Position.fromString("2"));
    assertThrows(Exception.class, () -> Position.fromString("abc2"));
    assertThrows(Exception.class, () -> Position.fromString("2a"));
    assertThrows(Exception.class, () -> Position.fromString("ö2"));
  }

  @Test
  public void testToString() throws Exception {
    assertEquals("C7", Position.fromString("C7").toString());
  }
}
