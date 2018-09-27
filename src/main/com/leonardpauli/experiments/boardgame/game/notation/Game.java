package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.*;

import java.util.ArrayList;

public class Game implements Token {
  public ArrayList<Tag> tags;
  public ArrayList<Round> rounds;

  public Token[] getInnerTokens() {
    return new Token[] {
      new AndToken(
          new Token[] {
            new OptionalToken(new RepeatToken(() -> new Tag.Section())),
            new RepeatToken(() -> new Round())
          })
    };
  }

  @Override
  public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
    AndToken a = (AndToken) at;
    for (Token ats : a.getTokens()) {
      if (ats instanceof OptionalToken) ats = ((OptionalToken) ats).getToken();
      RepeatToken rt = (RepeatToken) ats;
      for (Token t : rt.getTokens()) {
        if (t instanceof Tag) tags.add((Tag) t);
        else if (t instanceof Round) rounds.add((Round) t);
      }
    }
    return res;
  }
}
