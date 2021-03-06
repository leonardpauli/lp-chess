package com.leonardpauli.experiments.boardgame.board.layout;

import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.util.Size;

import java.util.Optional;

public class PrinterSquare implements Printer {
  LayoutSquare layout;

  public PrinterSquare(LayoutSquare layout) {
    this.layout = layout;
  }

  public String boardToString(Board board, Style style) {
    StringBuilder sb = new StringBuilder();
    Size size = layout.getSize();
    boolean pretty = style == Style.PRETTY || style == Style.PRETTY_WITH_NUMBERS;
    boolean numbers = style == Style.PRETTY_WITH_NUMBERS;

    if (pretty && numbers) sb.append("  ");
    if (pretty) sb.append("╭────────────────────────╮\n");

    for (int y = size.y - 1; y >= 0; y--) {
      if (pretty && numbers) sb.append(board.tiles[0][y].position.rowString() + " ");
      if (pretty) sb.append("│");

      for (int x = 0; x < size.x; x++) {
        Tile tile = board.tiles[x][y];
        Optional<Board.Marker> marker = board.getMarkerForTile(tile);
        boolean mark =
            marker.isPresent()
                && (marker.get() == Board.Marker.AVAILABLE
                    || marker.get() == Board.Marker.UNSAFE_AVAILABLE);
        boolean markUnsafe = marker.isPresent() && marker.get() == Board.Marker.UNSAFE;

        if (pretty) sb.append(mark ? "⟨" : " ");
        if (!pretty && (mark || markUnsafe) && !tile.hasPiece()) sb.append(mark ? "O" : "X");
        else sb.append(pretty ? tile.toCharPretty() : tile.toChar());
        if (pretty) sb.append(mark ? "⟩" : " ");
      }

      if (pretty) sb.append("│");
      sb.append("\n");
    }

    if (pretty && numbers) sb.append("  ");
    if (pretty) sb.append("╰────────────────────────╯\n");

    if (numbers) {
      sb.append("  ");
      if (pretty) sb.append(" ");
      for (int x = 0; x < size.x; x++) {
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
