package com.leonardpauli.experiments.boardgame.game.notation;

import com.leonardpauli.experiments.boardgame.actor.PieceType;
import com.leonardpauli.experiments.boardgame.board.tile.Position;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Token;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizeResult;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.Tokenizer;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.TokenizerException;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.AndToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OptionalToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.OrToken;
import com.leonardpauli.experiments.boardgame.game.notation.tokenizer.utils.PatternToken;
import com.leonardpauli.experiments.boardgame.util.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class Move implements Token {
  public static class Config {
    public Position.Optional origin = new Position.Optional();
    public Position.Optional target = new Position.Optional();
    public Optional<PieceType> type = Optional.empty();
    public boolean isPromotion = false;
    public boolean isCastling = false;
    public boolean isCheckmate = false;
    public boolean isCheck = false;
    public boolean isCapture = false;
    public AnnotationMove moveNote = AnnotationMove.NONE;
    public AnnotationState stateNote = AnnotationState.NONE;
    public Optional<Comment> comment = Optional.empty();

    @Override
    public String toString() {
      return Util.objectToString(this);
    }
  }

  public Move() {}

  public static Optional<Move> fromString(String code) {
    Move m = new Move();
    Tokenizer tokenizer = new Tokenizer(new ByteArrayInputStream(code.getBytes()));
    try {
      if (!tokenizer.tokenize(m).ok) return Optional.empty();
    } catch (TokenizerException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(m);
  }

  private Inner inner;
  private Comment comment;
  public String text;

  public Config getConfig() {
    Config c = inner.getConfig();
    c.comment = comment != null ? Optional.of(comment) : Optional.empty();
    return c;
  }

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
    text = str.substring(0, res.consumedCount);
    return res;
  }

  @Override
  public String toString() {
    return Util.objectToString(this);
  }

  public static class Inner implements Token {

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

    private AnnotationMove annotationMove = AnnotationMove.NONE;
    private AnnotationState annotationState = AnnotationState.NONE;

    public Position.Optional getOrigin() {
      if (promotion != null) return promotion.origin;
      if (pawnCaptureSimple != null) return pawnCaptureSimple.origin;
      if (moveNormal != null) return moveNormal.origin;
      return new Position.Optional();
    }

    public Position.Optional getTarget() {
      if (promotion != null) return promotion.origin;
      if (pawnCaptureSimple != null) return pawnCaptureSimple.target;
      if (moveNormal != null) return moveNormal.target;
      return new Position.Optional();
    }

    public Optional<PieceType> getType() {
      if (castling != null) return Optional.of(PieceType.KING);
      if (promotion != null) return Optional.of(promotion.newType);
      if (pawnCaptureSimple != null) return Optional.of(PieceType.PAWN);
      if (moveNormal != null) return moveNormal.type;
      return Optional.empty();
    }

    public boolean isPromotion() {
      return promotion != null;
    }

    public boolean isCastling() {
      return castling != null;
    }

    public boolean isCheckmate() {
      return isCheckmate;
    }

    public boolean isCheck() {
      return check != null;
    }

    public boolean isCapture() {
      return pawnCaptureSimple != null ? true : moveNormal != null ? moveNormal.isCapture : false;
    }

    public Config getConfig() {
      Config c = new Config();
      c.origin = getOrigin();
      c.target = getTarget();
      c.type = getType();
      c.isPromotion = isPromotion();
      c.isCastling = isCastling();
      c.isCheckmate = isCheckmate;
      c.isCheck = isCheck();
      c.isCapture = isCapture();
      c.moveNote = annotationMove;
      c.stateNote = annotationState;
      c.comment = Optional.empty();
      return c;
    }

    @Override
    public TokenizeResult handleInnerMatch(Token at, TokenizeResult res, String str) {
      for (Token t : ((AndToken) at).getTokens()) {
        if (t instanceof OrToken) {
          Token ot = ((OrToken) t).getMatchedToken();

          if (ot instanceof Castling) castling = (Castling) ot;
          else if (ot instanceof Promotion) promotion = (Promotion) ot;
          else if (ot instanceof PawnCaptureSimple) pawnCaptureSimple = (PawnCaptureSimple) ot;
          else if (ot instanceof MoveNormal) moveNormal = (MoveNormal) ot;

        } else if (t instanceof OptionalToken) {
          Token ot = ((OptionalToken) t).getToken();

          if (ot instanceof Check) check = (Check) ot;
          else if (ot instanceof PatternToken) isCheckmate = true;
          else if (ot instanceof AnnotationMove.Syntax)
            annotationMove = ((AnnotationMove.Syntax) ot).annotation;
          else if (ot instanceof AnnotationState.Syntax)
            annotationState = ((AnnotationState.Syntax) ot).annotation;
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

    static Pattern pattern = Pattern.compile("^([a-w])([a-w])");

    @Override
    public void handleMatch() {
      origin.setFrom(matcher.group(1));
      target.setFrom(matcher.group(2));
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
    public Optional<PieceType> type = Optional.empty();
    public Position.Optional origin = new Position.Optional();
    public Position.Optional target = new Position.Optional();
    public boolean isCapture = false;
    public boolean isEnPassant = false;

    @Override
    public Token[] getInnerTokens() {
      return new Token[] {
        new AndToken(
            new Token[] {
              new PieceType.Token(true),
              new OptionalToken(new PatternToken(Pattern.compile("^(x|:)"), "capture")),
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
      if (res.consumedCount == 0) return new TokenizeResult();
      if (res.consumedCount == 1 && !str.matches("^.($|\\s|,)")) return new TokenizeResult();

      for (Token t : ((AndToken) at).getTokens()) {
        if (t instanceof Position.Optional) {
          if (origin.isEmpty()) origin = (Position.Optional) t;
          else target = (Position.Optional) t;
        } else if (t instanceof PieceType.Token) {
          type = Optional.of(((PieceType.Token) t).type);
        } else if (t instanceof OptionalToken) {
          PatternToken pt = (PatternToken) ((OptionalToken) t).getToken();
          if ("capture".equals(pt.patternName)) {
            isCapture = true;
          } else {
            isEnPassant = true;
          }
        }
      }
      if (!origin.isEmpty() && target.isEmpty()) {
        target.setFrom(origin);
        origin.empty();
      }
      return res;
    }
  }
}
