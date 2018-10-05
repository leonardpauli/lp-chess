package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.State;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OptionalToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.RepeatToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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

  public HashMap<String, String> getKVTags() {
    HashMap<String, String> map = new HashMap<>();
    for (Tag.Section t : tags) {
      map.put(t.tag.key.toLowerCase(), t.tag.value);
    }
    return map;
  }

  public String getTitle() {
    HashMap<String, String> map = getKVTags();
    StringBuilder sb = new StringBuilder();
    // sb.append(map.get("event"));
    // sb.append(": ");
    sb.append(map.containsKey("white") ? map.get("white") : "white");
    sb.append(" vs ");
    sb.append(map.containsKey("black") ? map.get("black") : "black");
    if (map.containsKey("date")) {
      sb.append(", ");
      sb.append(map.get("date"));
    }
    sb.append(" (");
    sb.append(rounds.size() + 1);
    sb.append(") ");
    if (map.containsKey("result")) {
      sb.append(map.get("result"));
    }
    return sb.toString();
  }

  public Optional<State> getState() {
    if (rounds.size() == 0) return Optional.empty();
    Round r = rounds.get(rounds.size() - 1);
    if (r.status == null || r.status.state == null) return Optional.empty();
    return Optional.of(r.status.state);
  }
}
