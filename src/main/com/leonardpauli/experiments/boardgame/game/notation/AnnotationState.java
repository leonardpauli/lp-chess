package com.leonardpauli.experiments.boardgame.game.notation;

public enum AnnotationState {
  // descriptions originally from wikipedia
  NONE("", "no annotation"),
  EQUAL("=", "equal chances for both players"),
  WHITE_SLIGHT("+/=", "⩲", "White has a slight plus"),
  BLACK_SLIGHT("=/+", "⩱", "Black has a slight plus"),
  WHITE_CLEAR("+/−", "±", "White has a clear plus"),
  BLACK_CLEAR("−/+", "∓", "Black has a clear plus"),
  WHITE_WINNING("+−", "White has a winning advantage"),
  BLACK_WINNING("−+", "Black has a winning advantage"),
  UNCLEAR("∞", "unclear whether either side has an advantage"),
  COMPENSATED("=/∞", "whoever is down in material has compensation for it");

  public final String code;
  private final String codeAlternative;
  public final String description;

  AnnotationState(String code, String description) {
    this.code = code;
    this.codeAlternative = null;
    this.description = description;
  }

  AnnotationState(String code, String codeAlt, String description) {
    this.code = code;
    this.codeAlternative = codeAlt;
    this.description = description;
  }

  public String getCodeAlternative() {
    return codeAlternative;
  }

  public boolean hasCodeAlternative() {
    return codeAlternative != null;
  }
}
