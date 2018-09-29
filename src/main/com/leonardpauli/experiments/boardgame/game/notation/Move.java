package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OptionalToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OrToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.PatternToken;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.util.regex.Pattern;

public class Move implements Token {
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
    return comment != null && comment.hasComment();
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }

  private static Pattern simplePattern = Pattern.compile("^([^\\d\\n\\[]|\\d+(?!\\.|\\d))+");

  public static class Inner implements Token {
    public PieceType pieceType = null;

    public Position.Optional source = new Position.Optional();
    public Position.Optional target = new Position.Optional();

    @Override
    public Token[] getInnerTokens() {
      return new Token[] {
        new AndToken(
            new Token[] {
              new OrToken(
                  new Token[] {
                    new Castling(), new Promotion(), new PawnCaptureSimple(), new MoveNormal(),
                  }),
              new OptionalToken(new Check()),
              new OptionalToken(
                  new PatternToken(Pattern.compile("^ ?(#|mate|\\+\\+|‡|≠|X|x|×)"), "checkmate")),
              new OptionalToken(new AnnotationMove.Syntax()),
              new OptionalToken(new AnnotationState.Syntax()),
            })
      };
    }

    private Castling castling;
    private Promotion promotion;
    private PawnCaptureSimple pawnCaptureSimple;
    private MoveNormal moveNormal;
    
    private Check check;
    private boolean isCheckmate = false;

    public AnnotationMove annotationMove = AnnotationMove.NONE;
    public AnnotationState annotationState = AnnotationState.NONE;
    

    @Override
    public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
      for (Token t : ((AndToken) at).getTokens()) {
        if (t instanceof OrToken) {
          Token ot = ((OrToken) t).getMatchedToken();
          
          if (ot instanceof Castling) castling = (Castling) ot;
          else if (ot instanceof Promotion) promotion= (Promotion) ot;
          else if (ot instanceof PawnCaptureSimple) pawnCaptureSimple = (PawnCaptureSimple) ot;
          else if (ot instanceof MoveNormal) moveNormal = (MoveNormal) ot;
          
        } else if (t instanceof OptionalToken) {
          Token ot = ((OptionalToken) t).getToken();

          if (ot instanceof Check) check = (Check) ot;
          else if (ot instanceof PatternToken) isCheckmate = true;
          else if (ot instanceof AnnotationMove.Syntax) annotationMove = ((AnnotationMove.Syntax) ot).annotation;
          else if (ot instanceof AnnotationState.Syntax) annotationState = ((AnnotationState.Syntax) ot).annotation;
          
        }
      }
      return res;
    }

    @Override
    public String toString() {
      return Util.objectToString(this);
    }
  }

  public static class Castling extends PatternToken {
    static Pattern p = Pattern.compile("^[0O](-[0O])+");
    boolean kingside;

    @Override
    public void handleMatch() {
      kingside = matcher.end() == 3;
    }

    @Override
    public Pattern getDefaultPattern() {
      return p;
    }
  }

  public static class Promotion implements Token {
    Position.Optional origin;
    PieceType newType;

    @Override
    public Token[] getInnerTokens() {
      return new Token[] {
        new AndToken(
            new Token[] {
              new Position.Optional(false),
              new PatternToken(Pattern.compile("^(|=|\\()")),
              new PieceType.Token(false),
              new PatternToken(Pattern.compile("^(|\\))"))
            })
      };
    }

    @Override
    public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
      for (Token t : ((AndToken) at).getTokens()) {
        if (t instanceof Position.Optional) origin = (Position.Optional) t;
        else if (t instanceof PieceType.Token) newType = ((PieceType.Token) t).type;
      }
      return res;
    }
  }

  public static class PawnCaptureSimple extends PatternToken {
    Position.Optional origin = new Position.Optional();
    Position.Optional target = new Position.Optional();

    static Pattern pattern = Pattern.compile("^([a-z])([a-z])");

    @Override
    public void handleMatch() {
      origin.setFromString(matcher.group(1));
      target.setFromString(matcher.group(2));
    }

    @Override
    public Pattern getDefaultPattern() {
      return pattern;
    }
  }

  public static class Check extends PatternToken {
    static Pattern p =
        Pattern.compile("^(?<default>\\+|†|ch)|(?<double>dbl ch|\\+\\+)|(?<discovered>dis ch)");
    public boolean isDouble = false;
    public boolean isDiscovered = false;

    @Override
    public void handleMatch() throws Exception {
      if (matcher.group("double") != null) isDouble = true;
      if (matcher.group("discovered") != null) isDiscovered = true;
    }

    @Override
    public Pattern getDefaultPattern() {
      return p;
    }
  }

  public static class MoveNormal implements Token {
    public PieceType type;
    public Position.Optional origin;
    public Position.Optional target;
    public boolean isCapture = false;
    public boolean isEnPassant = false;

    @Override
    public Token[] getInnerTokens() {
      return new Token[] {
        new AndToken(
            new Token[] {
              new PieceType.Token(true),
              new Position.Optional(true),
              new OptionalToken(new PatternToken(Pattern.compile("^(x|:)"), "capture")),
              new Position.Optional(true),
              new OptionalToken(
                  new PatternToken(Pattern.compile("^ ?:?(?<enpassant>e\\.p\\.):?"), "enpassant"))
            })
      };
    }

    @Override
    public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
      for (Token t : ((AndToken) at).getTokens()) {
        if (t instanceof Position.Optional) {
          if (origin == null) origin = (Position.Optional) t;
          else target = (Position.Optional) t;
        } else if (t instanceof PieceType.Token) {
          type = ((PieceType.Token) t).type;
        } else if (t instanceof OptionalToken) {
          PatternToken pt = (PatternToken) ((OptionalToken) t).getToken();
          if ("capture".equals(pt.patternName)) {
            isCapture = true;
          } else {
            isEnPassant = true;
          }
        }
      }
      return res;
    }
  }
  
}
