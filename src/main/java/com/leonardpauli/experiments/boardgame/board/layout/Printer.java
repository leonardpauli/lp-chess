package com.leonardpauli.experiments.boardgame.board.layout;

import com.leonardpauli.experiments.boardgame.board.Board;

public interface Printer {
  enum Style {
    PLAIN,
    PRETTY,
    PRETTY_WITH_NUMBERS
  }

  String boardToString(Board board, Style style);
}
