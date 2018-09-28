package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizerException;
import com.leonardpauli.experiments.boardgame.util.Util;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class FileTest {

  @Test
  void loadNextGame() throws IOException, TokenizerException {
    File f = new File(File.class.getResourceAsStream("carlsen-excerpt.pgn"));
    f.loadNextGame();
    for (Comment c : f.comments) System.out.println(Util.objectToString(c));
  }
}
