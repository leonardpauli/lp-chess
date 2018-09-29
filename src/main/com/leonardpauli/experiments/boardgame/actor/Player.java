package com.leonardpauli.experiments.boardgame.actor;

import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.util.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Player {
  public String name;
  Color color;

  public Home home;
  boolean alive = true;
  public List<Piece> pieces = new ArrayList<Piece>();

  public Player(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  public Player(int ordinal) {
    if (ordinal == 0) {
      color = Color.white;
      name = "White";
    } else if (ordinal == 1) {
      color = Color.black;
      name = "Black";
    } else {
      color = Color.fromHSL(ordinal / 10f, 0.5f, 0.5f);
      name = "Player " + Integer.toString(1 + ordinal);
    }
  }

  // piece

  public Piece addPiece(Piece piece) {
    pieces.add(piece);
    piece.owner = this;
    return piece;
  }

  public Piece[] getAlivePieces() {
    Piece[] alivePieces = new Piece[pieces.size()];
    int i = 0;
    for (Piece piece : pieces) if (piece.isAlive()) alivePieces[i++] = piece;
    return Arrays.copyOf(alivePieces, i);
  }

  public List<Movement> getAvailableMovements(Board board) {
    List<Movement> allMovements = new ArrayList<>();
    for (Piece piece : getAlivePieces()) {
      try {
        List<Movement> movements = board.movement.getAvailable(piece);
        allMovements.addAll(movements);
      } catch (GameException e) {
        e.printStackTrace();
      }
    }
    return allMovements;
  }

  // equals
  // TODO: use better id

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Player)) return false;
    Player p = (Player) o;
    return p.name.equals(name) && p.color.getName().equals(color.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, color.getName());
  }
}
