package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.OptionalToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Move implements Token {
  public static class Inner implements Token {
    public PieceType pieceType = null;

    // TODO: NaN or better way to signal unknown (partial in any of x/y)
    public Position source = new Position(-1, -1);
    public Position target = new Position(-1, -1);

    /*
    public boolean isEnpassant = false;
    public boolean isPromotion = false;
    public PieceType promotionTargetPieceType = null;
    public boolean isCastling = false;
    public boolean castlingQueenside = false; // kingside

    public AnnotationMove annotationMove = AnnotationMove.NONE;
    public AnnotationState annotationState = AnnotationState.NONE;
    */

    public String text;
    private static Pattern pattern = Pattern.compile("^([^\\d\\n\\[]|\\d+(?!\\.|\\d))+");

    // TODO: followed by Comment.Part

    @Override
    public TokenizeResult getMatchResult(String str) {
      Matcher m = pattern.matcher(str);
      if (!m.find()) return new TokenizeResult();
      text = m.group();
      return new TokenizeResult(m.end());
    }

    @Override
    public String toString() {
      return Util.objectToString(this);
    }
  }

  public Inner inner;
  public Comment comment;

  public Token[] getInnerTokens() {
    return new Token[] {
      new AndToken(
          new Token[] {
            new Inner(), new OptionalToken(new Comment.Section()),
          })
    };
  }

  @Override
  public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
    AndToken a = (AndToken) at;
    for (Token t : a.getTokens()) {
      if (t instanceof Inner) inner = (Inner) t;
      else if (t instanceof OptionalToken) {
        Comment.Section ct = (Comment.Section) ((OptionalToken) t).getToken();
        comment = ct.comment;
      }
    }
    return res;
  }

  public boolean hasComment() {
    return comment != null;
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }
}
