package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Tokenizer;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizerException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveTest {

  @Test
  public void testInner() throws IOException, TokenizerException {
    Move s = new Move();
    Tokenizer tokenizer = new Tokenizer(new ByteArrayInputStream("e8".getBytes()));
    tokenizer.tokenize(s);
    assertEquals(
        "Config{origin: Optional{x: OptionalInt.empty, y: OptionalInt.empty}, target: Optional{x: OptionalInt[4], y: OptionalInt[7]}, type: PAWN, moveNote: NONE, stateNote: NONE, comment: Empty}",
        s.getConfig().toString());
  }

  @Test
  public void testFromStringQd2() {
    Move.Config c = Move.fromString("Qd2").get().getConfig();
    assertTrue(c.origin.isEmpty());
    assertEquals("D2", c.target.getPosition(0, 0).toString());
    assertEquals(PieceType.QUEEN, c.type.get());
  }

  @Test
  public void testFromStringNbd7() {
    Move.Config c = Move.fromString("Nbd7").get().getConfig();
    assertTrue(!c.origin.hasY());
    assertEquals("B", c.origin.getPosition(0, 0).colString());
    assertEquals("D7", c.target.getPosition(0, 0).toString());
    assertEquals(PieceType.KNIGHT, c.type.get());
  }

  @Test
  public void testFromStringCastling() {
    Move.Config c = Move.fromString("O-O").get().getConfig();
    assertTrue(c.origin.isEmpty());
    assertTrue(c.target.isEmpty());
    assertTrue(c.isCastling);
    assertEquals(PieceType.KING, c.type.get());
  }
}
