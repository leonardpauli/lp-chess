package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.Integer.max;

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

  public int getCodeCompareValue() {
    return max(code.length(), hasCodeAlternative() ? codeAlternative.length() : 0);
  }

  public static class Syntax implements Token {
    private static AnnotationState[] sorted;

    static {
      sorted = AnnotationState.values();
      Arrays.sort(sorted, Comparator.comparing(AnnotationState::getCodeCompareValue));
      reverseArr(sorted);
    }

    static <T> void reverseArr(T[] xs) {
      for (int s = 0, e = xs.length - 1; s < e; s++, e--) {
        T tmp = xs[s];
        xs[s] = xs[e];
        xs[e] = tmp;
      }
    }

    AnnotationState annotation;

    @Override
    public TokenizeResult getMatchResult(String str) {
      int l = 0;
      for (AnnotationState m : sorted) {
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
