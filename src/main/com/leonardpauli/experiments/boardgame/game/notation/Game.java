package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OptionalToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.RepeatToken;

import java.util.ArrayList;

public class Game implements Token {
  public ArrayList<Tag.Section> tags = new ArrayList<>();
  public ArrayList<Round> rounds = new ArrayList<>();

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
        if (t instanceof Tag.Section) tags.add((Tag.Section) t);
        else if (t instanceof Round) rounds.add((Round) t);
      }
    }
    return res;
  }
}
