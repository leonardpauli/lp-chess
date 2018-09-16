package com.leonardpauli.experiments.boardgame.chess;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length==0) {
			startInteractiveREPL(new Scanner(System.in));
		} else if (args[0].equals("about")) {
			printAbout();
		} else {
			printHelp();
		}
	}

	private static void doTurn(ChessGame game, Scanner scanner) throws ChessException {
		String command;

		System.out.print(game.getCurrentPlayer().name+"'s turn. Enter source tile (eg. A4): ");
		command = scanner.next();
		Position source = Position.fromString(command);
		Piece piece = game.board.pieceAt(source);

		System.out.print("Enter target tile: ");
		command = scanner.next();
		Position target = Position.fromString(command);

		Movement movement = game.board.movement.getAvailable(piece, target).get(0);
		game.playMove(new Move(game.getCurrentPlayer(), piece, movement));
	}

	private static void startInteractiveREPL(Scanner scanner) throws Exception {
		System.out.println("Welcome to Chess!");
		System.out.println("\n");

		ChessGame game = new ChessGame();

		while (game.getState()==State.DEFAULT) {

			System.out.println("\n\n\n");
			System.out.println(game.board.toString(Printer.Style.PRETTY_WITH_NUMBERS));

			try {
				doTurn(game, scanner);
			} catch (ChessException e) {
				System.out.println("Try again! ("+e.getMessage()+")");
			}
		}

		// TODO
		System.out.println("State changed! To be continued...");

	}

	private static void printHelp() {
		System.out.println("usage:"+
			"\n\tno arguments: start an interactive game"+
			"\n\tabout: show about page");
	}

	private static void printAbout() {
		System.out.println("About\n"+
			"com.leonardpauli.experiments.boardgame.chess\n"+
			"github.com/LeonardPauli/chess - java version\n"+
			"----\n\n"+
			"A Chess flavoured GameBoard implemented using edges of movements between "+
			"tiles instead of a usual hardcoded 8x8 board. The idea is that this will "+
			"allow for many variations of rules and board layouts with minimal code "+
			"change (eg. chess with three kingdoms using a pentagon board).\n"+
			"\nInitially created as a project in the indatapluplus course at KTH."+
			"\n----\n"+
			"Created by Leonard Pauli, sep 2018\n"+
			"Copyright © Leonard Pauli, 2018\n");
	}

}
