package com.leonardpauli.experiments.boardgame.board.tile;

import com.leonardpauli.experiments.boardgame.game.GameException;
import org.junit.jupiter.api.Test;

import static com.leonardpauli.experiments.boardgame.board.tile.EdgeType.*;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTypeTest {

  @Test
  public void testCanBeTurned() {
    assertTrue(RIGHT.canBeTurned());
    assertTrue(LEFT.canBeTurned());
    assertTrue(UP.canBeTurned());
    assertTrue(DOWN.canBeTurned());
    assertFalse(ANY.canBeTurned());
  }

  @Test
  public void testGetTurnedOnce() throws GameException {
    assertEquals(UP, RIGHT.getTurned());
    assertEquals(RIGHT.getTurned(1), RIGHT.getTurned());
  }

  @Test
  public void testGetTurned() throws GameException {
    assertEquals(UP.getTurned(0), UP.getTurned(4));
    assertEquals(UP.getTurned(1), UP.getTurned(5));
    assertEquals(UP.getTurned(1), LEFT);
    assertThrows(GameException.class, ANY::getTurned);
  }

  @Test
  public void testGetTurns() throws GameException {
    assertEquals(0, RIGHT.getTurns(RIGHT));
    assertEquals(3, RIGHT.getTurns(DOWN));
    assertEquals(1, DOWN.getTurns(RIGHT));
    assertEquals(1, RIGHT.getTurns(UP));
    assertThrows(GameException.class, () -> RIGHT.getTurns(ANY));
    assertThrows(GameException.class, () -> ANY.getTurns(RIGHT));
  }

  @Test
  public void testTurnedPath() throws GameException {
    assertEquals(EdgeType.getPath("").length, 0);
    assertArrayEquals(EdgeType.getPath(""), EdgeType.turnedPath(new EdgeType[] {}, 0));
    assertArrayEquals(EdgeType.getPath("^"), EdgeType.turnedPath(new EdgeType[] {UP}, 0));
    assertArrayEquals(EdgeType.getPath("<"), EdgeType.turnedPath(new EdgeType[] {UP}, 1));
    assertArrayEquals(EdgeType.getPath("<"), EdgeType.turnedPath(new EdgeType[] {UP}, 5));
    assertArrayEquals(EdgeType.getPath("<<^>v"), EdgeType.turnedPath(EdgeType.getPath("^^>v<"), 1));
  }

  @Test
  public void testFromCode() throws GameException {
    assertEquals(UP, EdgeType.fromCode('^'));
    assertThrows(GameException.class, () -> EdgeType.fromCode('x'));
  }

  @Test
  public void testGetPath() throws GameException {
    assertArrayEquals(new EdgeType[] {UP, LEFT, RIGHT, DOWN, DOWN}, EdgeType.getPath("^<>vv"));
    assertArrayEquals(new EdgeType[] {}, EdgeType.getPath(""));
  }

  @Test
  public void testStringFromPath() {
    assertEquals("", EdgeType.stringFromPath(new EdgeType[] {}));
    assertEquals(
        "^<>vv.", EdgeType.stringFromPath(new EdgeType[] {UP, LEFT, RIGHT, DOWN, DOWN, ANY}));
  }
}
