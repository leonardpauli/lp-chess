package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Tokenizer;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizerException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTest {

  @Test
  void inner() throws IOException, TokenizerException {
    Move.Inner s = new Move.Inner();
    Tokenizer tnzr = new Tokenizer(new ByteArrayInputStream("e8".getBytes()));
    TokenizeResult r = tnzr.tokenize(s);
    assertEquals(
        "Config{origin: Optional{x: OptionalInt.empty, y: OptionalInt.empty}, target: Optional{x: OptionalInt[4], y: OptionalInt[7]}, type: PAWN, moveNote: NONE, stateNote: NONE, comment: Empty}",
        s.getConfig().toString());
  }
}
