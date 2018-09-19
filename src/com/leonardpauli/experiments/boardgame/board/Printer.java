package com.leonardpauli.experiments.boardgame.board;

import com.leonardpauli.experiments.boardgame.board.tile.Tile;

public class Printer {
  public enum Style {
    PLAIN,
    PRETTY,
    PRETTY_WITH_NUMBERS
  };

  public static String boardToString(Board board, Style style) {
    StringBuilder sb = new StringBuilder();
    boolean pretty = style == Style.PRETTY || style == Style.PRETTY_WITH_NUMBERS;
    boolean numbers = style == Style.PRETTY_WITH_NUMBERS;

    if (pretty && numbers) sb.append("  ");
    if (pretty) sb.append("╭────────────────────────╮\n");

    for (int y = board.size.y - 1; y >= 0; y--) {
      if (pretty && numbers) sb.append(board.tiles[0][y].position.rowString() + " ");
      if (pretty) sb.append("│");

      for (int x = 0; x < board.size.x; x++) {
        Tile tile = board.tiles[x][y];
        if (pretty) sb.append(" ");
        sb.append(pretty ? tile.toCharPretty() : tile.toChar());
        if (pretty) sb.append(" ");
      }

      if (pretty) sb.append("│");
      sb.append("\n");
    }

    if (pretty && numbers) sb.append("  ");
    if (pretty) sb.append("╰────────────────────────╯\n");

    if (numbers) {
      sb.append("  ");
      if (pretty) sb.append(" ");
      for (int x = 0; x < board.size.x; x++) {
        Tile tile = board.tiles[x][0];
        if (pretty) sb.append(" ");
        sb.append(tile.position.colString());
        if (pretty) sb.append(" ");
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
