package com.leonardpauli.experiments.boardgame.chess;

import java.util.List;
import java.util.ArrayList;


public class ChessGame {
	Player[] players = new Player[2]; // order determines play order
	Board board;
	List<Round> rounds = new ArrayList<Round>();

	public static void ChessGame() {
		this.players = [
			new Player("White", "white"),
			new Player("Black", "black"),
		];
		this.board = new Board();
	}

	public Round currentRound() {
		return rounds.get(rounds.size()-1);
	}
	public Player getCurrentPlayer() {
		Round round = currentRound();
		return players[round.moves.size() % players.length];
	}
}

class Player {
	String name;
	String color;
	Player(String name, String color) {
		this.name = name; this.color = color;
	}
}

class Board {
	Tile[][] tiles;
	Size size = new Size(8, 8);

	Board() {
		tiles = new Tiles[size.x][size.y];
		for (int x = 0; x<size.x; x++) {
			for (int y = 0; y<size.y; y++) {
				Tile tile = tiles[x][y] = new Tile();
				tile.position = new Position(x, y);
			}	
		}
	};
}

class Point {public int x; public int y; Point(int x, int y) {this.x = x; this.y = y;}}
class Size extends Point {Size(int x, int y) {super(x, y)}}
class Position extends Point {
	Position(int x, int y) {super(x, y)}

}

class Tile {
	public Position position;
	Tile(Position position) {this.position = position;}
}

class Round {
	public List<Move> moves = new ArrayList<Move>();
	Round() {}
}

class Move {
	Player player;
	Move(Player player) {this.player = player;}
}