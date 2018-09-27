package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class File implements Token {
  private Tokenizer tokenizer;
  public String filename;
  private boolean noNextGameFound = true;

  public ArrayList<Game> games = new ArrayList<>();
  public ArrayList<Comment> comments = new ArrayList<>();

  public File(InputStream inputStream) throws IOException, TokenizerException {
    this.tokenizer = new Tokenizer(inputStream);
    this.filename = "anonymous";
    loadNextGame();
  }

  public Token[] getInnerTokens() {
    return new Token[] {new Comment(), new Whitespace(), new Game()};
  }

  @Override
  public TokenizeResult handleInnerMatch(Token t, TokenizeResult res, String str) {
    if (t instanceof Comment) comments.add((Comment) t);
    else if (t instanceof Game) games.add((Game) t);
    return res;
  }

  public boolean loadNextGame() throws IOException, TokenizerException {
    TokenizeResult res = null;
    int prevGamesCount = games.size();
    boolean addedGame = false;
    while ((res = tokenizer.tokenize(this)).ok) {
      tokenizer.increaseConsumedCount(res.consumedCount);
      addedGame = prevGamesCount < games.size();
      if (addedGame) break;
    }
    noNextGameFound = !addedGame;
    return addedGame;
  }

  public boolean mightHaveNextGame() {
    return !noNextGameFound;
  }
}
