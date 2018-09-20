package com.leonardpauli.experiments.boardgame.chess;

import com.leonardpauli.experiments.boardgame.actor.Piece;
import com.leonardpauli.experiments.boardgame.board.layout.PrinterSquare;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.game.Move;
import com.leonardpauli.experiments.boardgame.game.State;

import java.util.List;
import java.util.Scanner;

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

    Piece firstMovable = game.getCurrentPlayer().getFirstMovablePiece(game.board);
    System.out.print(
        game.getCurrentPlayer().name
            + "'s turn. Enter source tile (eg. "
            + firstMovable.tile.position.toString()
            + "): ");

    command = scanner.next();
    Position source = Position.fromString(command);
    Piece piece = game.board.pieceAt(source);
    // System.out.println(piece);

    List<Movement> movements = game.board.movement.getAvailable(piece);
    if (movements.size() == 0) {
      System.out.println("Not movable, try another one");
      return;
    }

    String egTargetPos = movements.get(0).edge.target.position.toString();
    System.out.print("Enter target tile (eg. " + egTargetPos + "): ");
    command = scanner.next();
    Position target = Position.fromString(command);

    List<Movement> movementsFinal = game.board.movement.getAvailable(piece, target);
    if (movementsFinal.size() == 0) {
      System.out.println("Not movable there, try again");
      return;
    }

    Movement movement = movementsFinal.get(0);
    game.playMove(new Move(game.getCurrentPlayer(), piece, movement));
  }

  private static void startInteractiveREPL(Scanner scanner) throws Exception {
    System.out.println("Welcome to Chess!");

    ChessGame game = new ChessGame();

    while (game.getState() == State.DEFAULT) {

      System.out.println("\n");
      System.out.println(game.board.toString(PrinterSquare.Style.PRETTY_WITH_NUMBERS));

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
