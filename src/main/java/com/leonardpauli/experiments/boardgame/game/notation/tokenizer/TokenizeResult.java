package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import com.leonardpauli.experiments.boardgame.util.Util;

public class TokenizeResult {
  public final boolean ok;
  public final boolean needsMore;
  public final int consumedCount;
  int maxNeededStringSize = 32768;

  public TokenizeResult() {
    this.ok = false;
    this.needsMore = false;
    this.consumedCount = 0;
  }

  public TokenizeResult(boolean needsMore) {
    this.ok = false;
    this.needsMore = needsMore;
    this.consumedCount = 0;
  }

  public TokenizeResult(int consumedCount) {
    this.ok = true;
    this.needsMore = false;
    this.consumedCount = consumedCount;
  }

  public void setMaxNeededStringSize(int maxNeededStringSize) {
    this.maxNeededStringSize = maxNeededStringSize;
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
