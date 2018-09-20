package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.State;

public class Round {
  public int ordinal = 0;
  public Move[] moves;
  public State state = State.DEFAULT;
  private boolean matingPlayerIsWhite = true;

  public boolean didWhiteCauseMate() {
    return matingPlayerIsWhite && state == State.CHECKMATE;
  }

  public void setDidWhiteCauseMate(boolean caused) {
    matingPlayerIsWhite = caused;
  }
}
