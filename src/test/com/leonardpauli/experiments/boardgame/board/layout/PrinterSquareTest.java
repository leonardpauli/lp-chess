package com.leonardpauli.experiments.boardgame.board.layout;

import com.leonardpauli.experiments.boardgame.board.Board;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrinterSquareTest {

  private static HashMap<String, String> expectedBoards;

  static String getExpectedBoard(String key) throws Exception {
    if (expectedBoards == null) {
      expectedBoards = PrinterSquareTest.getExpectedBoards();
    }
    return expectedBoards.get(key);
  };

  static HashMap<String, String> getExpectedBoards() throws URISyntaxException, IOException {
    URL fu = PrinterSquareTest.class.getResource("boards.txt");
    String contents = new String(Files.readAllBytes(Paths.get(fu.toURI())));

    String[] sectionsRaw = contents.split("(^|\n)# ");
    HashMap<String, String> sections = new HashMap<>();
    for (String sectionRaw : sectionsRaw) {
      int idx = sectionRaw.indexOf("\n");
      if (idx == -1) continue;
      String key = sectionRaw.substring(0, idx);
      String value = sectionRaw.substring(idx + 1);
      sections.put(key, value);
    }

    return sections;
  }

  @Test
  void boardToString() throws Exception {
    Board board = new Board();
    assertEquals(
        PrinterSquareTest.getExpectedBoard("empty"), board.toString(PrinterSquare.Style.PLAIN));
    assertEquals(
        PrinterSquareTest.getExpectedBoard("empty_pretty"),
        board.toString(PrinterSquare.Style.PRETTY));
    assertEquals(
        PrinterSquareTest.getExpectedBoard("empty_pretty_with_numbers"),
        board.toString(PrinterSquare.Style.PRETTY_WITH_NUMBERS));
  }
}
