package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Tokenizer;

import java.io.InputStream;
import java.util.ArrayList;

public class File {
  private Tokenizer tokenizer;
  public String filename;
  private boolean noNextGameFound = true;

  public ArrayList<Game> games = new ArrayList<>();
  public ArrayList<Comment> comment = new ArrayList<>();

  public File(InputStream inputStream) {
    this.tokenizer = new Tokenizer(inputStream);
    this.filename = "anonymous";
    // loadNextGame();
  }

  // public boolean loadNextGame() {
  // 	loadNextComments();
  // 	Game game = new Game();
  // 	boolean matched = tokenizer.tokenize(game);
  // 	if (matched) games.add(game);
  // 	noNextGameFound = !matched;
  // 	return matched;
  // }
  // public boolean mightHaveNextGame() {
  // 	return !noNextGameFound;
  // }

  // private boolean loadNextComment() {
  // 	Comment comment = new Comment();
  // 	boolean matched = tokenizer.tokenize(comment);
  // 	if (matched) comments.add(comment);
  // 	return matched;
  // }
  // private boolean loadNextComments() {
  // 	boolean matched = true;
  // 	boolean matchedAtAll = false;
  // 	while ((matched = loadNextComment())) {
  // 		matchedAtAll = true;
  // 	};
  // 	return matchedAtAll;
  // }

}
