package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.Integer.max;

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

  public int getCodeCompareValue() {
    return max(code.length(), hasCodeAlternative() ? codeAlternative.length() : 0);
  }

  public static class Syntax implements Token {
    private static AnnotationMove[] sorted;

    static {
      sorted = AnnotationMove.values();
      Arrays.sort(sorted, Comparator.comparing(AnnotationMove::getCodeCompareValue));
      reverseArr(sorted);
    }

    static <T> void reverseArr(T[] xs) {
      for (int s = 0, e = xs.length - 1; s < e; s++, e--) {
        T tmp = xs[s];
        xs[s] = xs[e];
        xs[e] = tmp;
      }
    }

    AnnotationMove annotation;

    @Override
    public TokenizeResult getMatchResult(String str) {
      int l = 0;
      for (AnnotationMove m : sorted) {
        if (str.startsWith(m.code)) {
          l = m.code.length();
        } else if (m.hasCodeAlternative() && str.startsWith(m.getCodeAlternative())) {
          l = m.getCodeAlternative().length();
        } else {
          continue;
        }
        annotation = m;
        break;
      }
      if (l == 0) return new TokenizeResult();
      return new TokenizeResult(0);
    }
  }
}
