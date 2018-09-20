package com.leonardpauli.experiments.boardgame.game.notation;

public enum AnnotationMove {
  // descriptions originally from wikipedia
  NONE("", "no annotation"),
  EXELLENT("!", "an excellent move"),
  GOOD_SURPRISING("!!", "a particularly good—and usually surprising—move"),
  BAD("?", "a bad move; a mistake"),
  BAD_WHY("??", "a blunder"),
  INTERESTING_WHAT("!?", "an interesting move that may not be best"),
  WHAT_WHY_THOUGH("?!", "a dubious move or move that may turn out to be bad"),
  BETTER("⌓", "a better move than the one played"),
  ONLY_OK("□", "the only reasonable move, or the only move available"),
  NOVELTY("TN", "N", "a theoretical novelty");

  public final String code;
  private final String codeAlternative;
  public final String description;

  AnnotationMove(String code, String description) {
    this.code = code;
    this.codeAlternative = null;
    this.description = description;
  }

  AnnotationMove(String code, String codeAlt, String description) {
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
