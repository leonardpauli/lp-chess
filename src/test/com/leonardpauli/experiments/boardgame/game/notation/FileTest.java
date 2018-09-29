package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.game.State;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizerException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileTest {

  @Test
  void loadNextGame() throws IOException, TokenizerException {
    File f = new File(File.class.getResourceAsStream("carlsen-excerpt.pgn"));
    f.loadNextGame();
    assertEquals(2, f.comments.size());
    assertTrue(1 <= f.games.size()); // TODO: why two at first? because fit in buffer? supposed?

    f.loadNextGame();
    f.loadNextGame();
    assertEquals(3, f.games.size());

    // TODO: moves should be 2, not 1, per round
    // TODO: parsing of move
  }

  @Test
  void loadNextGameSyntax() throws IOException, TokenizerException {
    File f = new File(File.class.getResourceAsStream("carlsen-excerpt.pgn"));
    f.loadNextGame();
    f.loadNextGame();
    f.loadNextGame();
    assertEquals(3, f.games.size());

    assertEquals(10, f.games.get(0).tags.size());
    assertEquals(23, f.games.get(0).rounds.size());
    assertEquals(1, f.games.get(0).rounds.get(0).ordinal.nr);
    assertEquals(2, f.games.get(0).rounds.get(0).moves.length);
    Move.Config c = f.games.get(0).rounds.get(0).moves[1].inner.getConfig();
    assertEquals(PieceType.KNIGHT, c.type.get());
    assertTrue(c.origin.isEmpty());
    assertEquals("F6", c.target.getPosition(0, 0).toString());
    assertEquals(State.DRAW, f.games.get(0).rounds.get(22).status.state);
  }
}
