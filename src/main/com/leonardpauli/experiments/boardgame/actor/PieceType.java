package com.leonardpauli.experiments.boardgame.actor;

import com.leonardpauli.experiments.boardgame.board.movement.MovementType;
import com.leonardpauli.experiments.boardgame.game.GameException;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.util.Color;

import static com.leonardpauli.experiments.boardgame.board.movement.MovementType.*;

public enum PieceType {
  KING("King", 20, "♚", "♔", new MovementType[] {ONE_STEP, CASTLING}),
  QUEEN("Queen", 9, "♛", "♕", new MovementType[] {STRAIGHT, DIAGONAL}),
  ROOK("Rook", 5, "♜", "♖", new MovementType[] {STRAIGHT}),
  KNIGHT("Knight", "N", 3, "♞", "♘", new MovementType[] {LMOVE}),
  BISHOP("Bishop", 3, "♝", "♗", new MovementType[] {DIAGONAL}),
  PAWN(
      "Pawn",
      "",
      1,
      "♟",
      "♙",
      new MovementType[] {FORWARD_ONE, FORWARD_TWO_FROM_HOME, ENPASSANT, PROMOTION});

  public final String title;
  public final String letter;
  private int defaultValue = 0;
  private String defaultChar = "◆︎";
  private String defaultCharBlack = "◇";
  public final MovementType[] movementTypes;

  PieceType(
      String title,
      String letter,
      int defaultValue,
      String defaultChar,
      String defaultCharBlack,
      MovementType[] movementTypes) {
    this.title = title;
    this.letter = letter;
    this.defaultValue = defaultValue;
    this.defaultChar = defaultChar;
    this.defaultCharBlack = defaultCharBlack;
    this.movementTypes = movementTypes;
  }

  PieceType(
      String title,
      int defaultValue,
      String defaultChar,
      String defaultCharBlack,
      MovementType[] movementTypes) {
    this(title, title.substring(0, 1), defaultValue, defaultChar, defaultCharBlack, movementTypes);
  }

  public static PieceType fromCharUnsafe(String letter) {
    for (PieceType t : PieceType.values()) {
      if (letter.equals(t.letter)
          || letter.equals(t.defaultChar)
          || letter.equals(t.defaultCharBlack)) return t;
    }
    return null;
  }

  public static PieceType fromChar(String letter) throws GameException {
    PieceType t = fromCharUnsafe(letter);
    if (t != null) return t;
    throw new GameException("piece type for letter " + letter + " not found");
  }

  // string

  public int getValue() {
    return defaultValue;
  }

  public String toChar(Color color) {
    return color == Color.black ? defaultCharBlack : defaultChar;
  }

  @Deprecated
  public String getLetter() {
    return letter;
  }

  static String[] getAssociatedChars() {
    PieceType[] types = PieceType.values();
    String[] xs = new String[types.length * 3];
    int i = 0;
    for (PieceType t : types) {
      xs[i++] = t.letter;
      xs[i++] = t.defaultChar;
      xs[i++] = t.defaultCharBlack;
    }
    return xs;
  }

  public static class Token
      implements com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token {
    public PieceType type;
    private boolean matchEmpty = true;

    public Token(boolean matchEmpty) {
      this.matchEmpty = matchEmpty;
    }

    @Override
    public TokenizeResult getMatchResult(String str) {
      if (str.length() > 0) {
        for (String x : PieceType.getAssociatedChars()) {
          if (str.startsWith(x)) {
            type = PieceType.fromCharUnsafe(x);
            return new TokenizeResult(x.length());
          }
        }
      }
      if (matchEmpty) {
        type = PAWN;
        return new TokenizeResult(0);
      }
      return new TokenizeResult();
    }
  }
}
