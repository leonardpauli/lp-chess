package com.leonardpauli.experiments.boardgame.chess;

import com.leonardpauli.experiments.boardgame.board.layout.PrinterSquare;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.game.Move;
import com.leonardpauli.experiments.boardgame.game.State;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      startInteractiveREPL(new Scanner(System.in));
    } else if (args[0].equals("about")) {
      printAbout();
    } else {
      printHelp();
    }
  }

  private static void doTurn(ChessGame game, Scanner scanner) throws GameException {
    String command;

    List<Movement> ms = game.getCurrentPlayer().getAvailableMovements(game.board);
    while (true) {
      game.board.setTileMarksFromAvailableMovements(ms.toArray(new Movement[] {}));
      System.out.println(game.board.toString(PrinterSquare.Style.PRETTY_WITH_NUMBERS));

      if (ms.size() == 0) throw new GameException("no movements available!");

      System.out.print(game.getCurrentPlayer().name + "'s turn. Enter move: ");
      command = scanner.next();

      ms = game.board.getMovementsForNotation(command, game.getCurrentPlayer());
      if (ms.size() == 0) {
        System.out.println("Not available, try another one");
        ms = game.getCurrentPlayer().getAvailableMovements(game.board);
      } else if (ms.size() > 1) {
        System.out.println("Ambiguous, try a bit more specific");
      } else break;
    }

    Movement movement = ms.get(0);
    game.playMove(new Move(game.getCurrentPlayer(), movement.edge.source.getPiece(), movement));
  }

  private static void startInteractiveREPL(Scanner scanner) throws Exception {
    System.out.println("Welcome to Chess!");
    System.out.println(
        "Moves are written in standard chess notation, eg. Kb1a3, Kh, or just a3 should do, see wikipedia :)");

    ChessGame game = new ChessGame();
    scanner.useDelimiter(Pattern.compile("\\s"));

    while (game.getState() == State.DEFAULT) {

      System.out.println("\n");

      try {
        doTurn(game, scanner);
      } catch (GameException e) {
        System.out.println("Try again! (" + e.getMessage() + ")");
        throw e;
      }
    }

    // TODO
    System.out.println("State changed! To be continued...");
  }

  private static void printHelp() {
    System.out.println(
        "usage:" + "\n\tno arguments: start an interactive game" + "\n\tabout: show about page");
  }

  private static void printAbout() {
    System.out.println(
        "About\n"
            + "com.leonardpauli.experiments.boardgame.chess\n"
            + "github.com/LeonardPauli/chess - java version\n"
            + "----\n\n"
            + "A Chess flavoured GameBoard implemented using edges of movements between "
            + "tiles instead of a usual hardcoded 8x8 board. The idea is that this will "
            + "allow for many variations of rules and board layouts with minimal code "
            + "change (eg. chess with three kingdoms using a pentagon board).\n"
            + "\nInitially created as a project in the indatapluplus course at KTH."
            + "\n----\n"
            + "Created by Leonard Pauli, sep 2018\n"
            + "Copyright © Leonard Pauli, 2018\n");
  }
}
