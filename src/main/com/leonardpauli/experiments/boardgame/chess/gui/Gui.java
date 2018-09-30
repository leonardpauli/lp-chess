package com.leonardpauli.experiments.boardgame.chess.gui;

import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.chess.ChessGame;
import com.leonardpauli.experiments.boardgame.game.Move;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;

public class Gui extends Application {
  private Point2D dragStart;
  private Point2D size = new Point2D(500, 500);
  private Board.Piece dragPiece;
  private ChessGame game;
  private Board board;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Group root = new Group();
    Scene scene = new Scene(root, size.getX(), size.getY());

    Rectangle bg = new Rectangle(size.getX(), size.getY());
    bg.setFill(Color.hsb(0, 0.07, 0.12 * 0));

    Point2D margin = new Point2D(10, 10);

    board = new Board();
    board.origin = margin.multiply(1);
    board.size = size.subtract(margin.multiply(2));

    game = new ChessGame();
    game.board.tilesAll.forEach(
        t -> {
          Board.Tile tile = new Board.Tile();
          tile.ref = t;

          for (Point2D p : game.board.layout.getNormalizedCornersForTile(t))
            tile.corners.add(new Point2D(p.getX(), 1 - p.getY()));

          tile.baseColor = t.color.getFXColor();
          tile.updateBgColor();
          board.tiles.add(tile);

          if (t.hasPiece()) {
            Board.Piece piece = new Board.Piece();
            piece.ref = t.getPiece();
            piece.tile = tile;
            piece.view.setText(
                t.getPiece().type.toChar(com.leonardpauli.experiments.boardgame.util.Color.white));
            Color c = t.getPiece().getColor().getFXColor();
            c =
                c.darker()
                    .interpolate(Color.hsb(20, 0.1, 0.5), 0.6)
                    .interpolate(Color.hsb(0, 0, 0.2), 0)
                    .interpolate(Color.hsb(20, 1, 0.5), 0.2)
                    .deriveColor(0, (1 - c.getBrightness()) * 0.4 + 0.6, 1, 1)
                    .brighter();
            piece.view.setFill(c);
            board.pieces.add(piece);
          }
        });

    board.resetView();

    board.tiles.forEach(t -> t.updateBgPath(board, 1));
    board.pieces.forEach(t -> t.updateLayout());

    root.getChildren().add(bg);
    root.getChildren().add(board.view);

    addMouseInteraction(stage, root);

    stage.setScene(scene);
    stage.setTitle("Chess");
    StageStyle style = StageStyle.UNDECORATED;
    stage.initStyle(style);
    stage.show();
  }

  public void addMouseInteraction(Stage stage, Group root) {
    root.setOnMousePressed(
        event -> {
          dragStart = new Point2D(event.getSceneX(), event.getSceneY());

          dragPiece = null;
          for (Board.Piece p : board.pieces) {
            if (p.view.getBoundsInParent().contains(dragStart)) {
              dragPiece = p;

              List<Movement> ms = dragPiece.ref.getAvailableMovements(game.board);
              updateBoardWithAvailableMovements(ms);

              break;
            }
          }
        });

    root.setOnMouseDragged(
        event -> {
          Point2D pos = new Point2D(event.getSceneX(), event.getSceneY());

          if (dragPiece != null) {
            Point2D destination = dragPiece.position.add(pos.subtract(dragStart));
            dragPiece.updatePosition(destination);

            return;
          }

          Point2D mouseScreenPos = new Point2D(event.getScreenX(), event.getScreenY());
          Point2D windowPos = mouseScreenPos.subtract(dragStart);
          stage.setX(windowPos.getX());
          stage.setY(windowPos.getY());
        });

    root.setOnMouseReleased(
        event -> {
          Point2D pos = new Point2D(event.getSceneX(), event.getSceneY());

          if (dragPiece != null) {
            dragPiece.updatePosition(dragPiece.position.add(pos.subtract(dragStart)));
            if (!tryMovePiece(dragPiece, pos)) {
              dragPiece.updatePosition(dragPiece.position);
            }
            updateBoard();
            dragPiece = null;
            return;
          }
        });
  }

  private boolean tryMovePiece(Board.Piece piece, Point2D destination) {
    Optional<Board.Tile> t = board.getTileAtPosition(destination);
    if (!t.isPresent()) return false;
    List<Movement> ms = piece.ref.getAvailableMovements(game.board, t.get().ref.position);
    if (ms.size() == 0) return false;
    Movement movement = ms.get(0);

    try {
      game.playMove(new Move(game.getCurrentPlayer(), piece.ref, movement));
    } catch (InvalidMoveException e) {
      return false;
    }

    if (movement.getCapturedPiece().isPresent()) {
      board.getPiece(t.get()).ifPresent(Board.Piece::setCaptured);
    }

    piece.tile = t.get();
    piece.updateLayout();

    return true;
  }

  private void updateBoard() {
    List<Movement> ms = game.getCurrentPlayer().getAvailableMovements(game.board);
    updateBoardWithAvailableMovements(ms);
  }

  private void updateBoardWithAvailableMovements(List<Movement> ms) {
    game.board.setTileMarksFromAvailableMovements(ms.toArray(new Movement[] {}));
    board.tiles.forEach(
        t -> {
          t.setMarked(game.board.getMarkerForTile(t.ref).isPresent());
        });
  }
}
