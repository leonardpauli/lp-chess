package com.leonardpauli.experiments.boardgame.board.tile;

import org.junit.jupiter.api.Test;

class EdgeTypeTest {

  @Test
  void canBeTurned() {
    // RIGHT, LEFT, TOP, BOTTOM
    // not ANY
  }

  @Test
  void getTurnedOnce() {
    // RIGHT -> TOP
    // RIGHT.getTurned() == RIGHT.getTurned(1)
  }

  @Test
  void getTurned() {
    // 0 == 4
    // 1 == 5
    // ANY throws
  }

  @Test
  void getTurns() {
    // RIGHT RIGHT == 0
    // RIGHT DOWN == 3
    // DOWN RIGHT == 3
    // RIGHT UP == 1
    // RIGHT ANY throws
  }

  @Test
  void turnedPath() {
    // "", 0 == ""
    // "^", 0 == "^"
    // "^", 1 == "<"
    // "^", 5 == "<"
    // "^^>v<", 1 == "<<^>v"
  }

  @Test
  void fromCode() {
    // "^" == [UP]
    // "x" throws
    // "" == []
  }

  @Test
  void getPath() {
    // [UP, LEFT, RIGHT, DOWN, DOWN] == "^<>vv"
    // [] == ""
  }

  @Test
  void stringFromPath() {
    // []
    // [UP, LEFT, RIGHT, DOWN, DOWN, ANY] == "^<>vv."
  }
}
