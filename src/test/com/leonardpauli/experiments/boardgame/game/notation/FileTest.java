package com.leonardpauli.experiments.boardgame.game.notation;

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

    // for (Comment c : f.comments) System.out.println(Util.objectToString(c));
    // for (Game c : f.games) System.out.println(Util.objectToString(c));
    // for (Game c : f.games) System.out.println(Util.objectToString(c.rounds.get(0).moves[0]));

    // TODO: moves should be 2, not 1, per round
    // TODO: parsing of move
  }
}
