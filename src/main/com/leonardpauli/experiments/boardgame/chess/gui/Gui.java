package com.leonardpauli.experiments.boardgame.chess.gui;

import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.chess.ChessGame;
import com.leonardpauli.experiments.boardgame.game.Move;
import com.leonardpauli.experiments.boardgame.game.notation.File;
import com.leonardpauli.experiments.boardgame.game.notation.Game;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

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
                    .deriveColor(
                        0,
                        (1 - c.getBrightness()) * 0.3 + 0.8,
                        (1 - c.getBrightness()) * 0.07 + 0.87,
                        1)
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
    root.getChildren().add(addMenuBar());

    addMouseInteraction(stage, root);
    addKeyboardInteraction(root);

    stage.setScene(scene);
    stage.setTitle("Chess");
    StageStyle style = StageStyle.UNDECORATED;
    stage.initStyle(style);
    stage.show();

    root.requestFocus(); // for keyboard
  }

  private void replayGame() {
    InputStream resource = Gui.class.getResourceAsStream("carlsen-excerpt.pgn");
    File file;
    try {
      file = new File(resource);
      file.loadNextGame();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    replayMoves(file.games.get(0), 0, 0);
  }

  private void replayMoves(Game g, int roundIdx, int moveIdx) {
    if (roundIdx >= g.rounds.size()) return;
    if (moveIdx >= g.rounds.get(roundIdx).moves.length) {
      replayMoves(g, roundIdx + 1, 0);
      return;
    }
    com.leonardpauli.experiments.boardgame.game.notation.Move move =
        g.rounds.get(roundIdx).moves[moveIdx];
    List<Movement> movements = game.board.getMovementsForMove(move, game.getCurrentPlayer());
    if (movements.size() == 0) {
      System.out.println("No move for" + move.toString());
      return;
    }
    playMovement(movements.get(0));

    Timer timer = new Timer();
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            replayMoves(g, roundIdx, moveIdx + 1);
          }
        },
        (long) 300);
  }

  private BorderPane addMenuBar() {
    MenuBar menuBar = new MenuBar();
    MenuItem mi = new MenuItem("Play PGN");
    menuBar.getMenus().add(new Menu("File", null, mi));

    mi.setOnAction(
        event -> {
          replayGame();
        });

    final String os = System.getProperty("os.name");
    if (os != null && os.startsWith("Mac")) menuBar.useSystemMenuBarProperty().set(true);

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(menuBar);
    return borderPane;
  }

  private void showForNotation(String command, boolean executeIfUnambiguous) {
    List<Movement> ms = game.board.getMovementsForNotation(command, game.getCurrentPlayer());
    System.out.println(command + ms.size());
    if (ms.size() == 0) {
      ms = game.getCurrentPlayer().getAvailableMovements(game.board);
      updateBoardWithAvailableMovements(ms);
    } else if (ms.size() > 1) {
      updateBoardWithAvailableMovements(ms);
    } else {
      updateBoardWithAvailableMovements(ms);
      if (executeIfUnambiguous) {
        playMovement(ms.get(0));
      }
    }
  }

  private void playMovement(Movement m) {
    Optional<Board.Tile> source = board.getTile(m.edge.source);
    Optional<Board.Tile> t = board.getTile(m.edge.target);
    if (!source.isPresent()) return;

    Board.Piece p = board.getPiece(source.get()).get();

    try {
      game.playMove(new Move(game.getCurrentPlayer(), m.edge.source.getPiece(), m));
    } catch (InvalidMoveException e) {
      return;
    }

    if (m.getCapturedPiece().isPresent()) {
      board.getPiece(t.get()).ifPresent(Board.Piece::setCaptured);
    }

    p.tile = t.get();
    p.updateLayout();
  }

  private String keySequence = "";

  private void addKeyboardInteraction(Group root) {
    root.setOnKeyTyped(
        event -> {
          if (event.getCharacter() != null
              && event.getCharacter().length() == 1
              && !event.getCharacter().equals("\r")) keySequence += event.getCharacter();
          showForNotation(keySequence, false);
        });
    root.setOnKeyPressed(
        event -> {
          if ("\n".equals(event.getCode().getChar())) {
            showForNotation(keySequence, true);
            keySequence = "";
          }
        });
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
