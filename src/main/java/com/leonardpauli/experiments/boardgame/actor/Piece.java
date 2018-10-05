package com.leonardpauli.experiments.boardgame.actor;

import com.leonardpauli.experiments.boardgame.board.Board;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.board.tile.Tile;
import com.leonardpauli.experiments.boardgame.util.Color;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.List;

public class Piece {
  public PieceType type;
  public Tile tile;
  public Player owner;
  private Tile homeTile;

  public Piece(PieceType type) {
    this.type = type;
  }

  public boolean isAtHome() {
    return tile == homeTile;
  }

  public void setHome(Tile tile) {
    homeTile = tile;
  }

  public boolean isAlive() {
    return tile != null;
  }

  public Color getColor() {
    return owner.color;
  }

  public String toChar() {
    return getColor() == Color.black
        ? type.getLetter().toLowerCase()
        : type.getLetter().toUpperCase();
  }

  public String toCharPretty() {
    return type.toChar(getColor());
  }

  public String toString() {
    return Util.objectToString(this);
  }

  public List<Movement> getAvailableMovements(Board board) {
    return board.movement.getAvailable(this);
  }

  public List<Movement> getAvailableMovements(Board board, Position destination) {
    return board.movement.getAvailable(this, destination);
  }
}
