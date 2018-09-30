package com.leonardpauli.experiments.boardgame.chess.gui;

import com.leonardpauli.experiments.boardgame.actor.Player;
import com.leonardpauli.experiments.boardgame.board.movement.CastlingMovement;
import com.leonardpauli.experiments.boardgame.board.movement.InvalidMoveException;
import com.leonardpauli.experiments.boardgame.board.movement.Movement;
import com.leonardpauli.experiments.boardgame.board.tile.Edge;
import com.leonardpauli.experiments.boardgame.board.tile.EdgeType;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class Gui extends Application {
  private Point2D dragStart;
  private Point2D size = new Point2D(500, 500);
  private Board.Piece dragPiece;
  private ChessGame game;
  private Board board;
  private boolean showGutter = true;
  private Menu playPgnMenu;
  private File pgnFile;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene scene = new Scene(root, size.getX(), size.getY());

    Rectangle bg = new Rectangle(size.getX(), size.getY());
    bg.setFill(Color.hsb(40, 0.17, 0.078));

    double marginV = (showGutter ? 20 : 10) + (isOsx() ? 15 : 30);
    Point2D margin = new Point2D(marginV, marginV);

    board = new Board();
    board.origin = margin.multiply(1);
    board.size = size.subtract(margin.multiply(2));

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

    newGame();
  }

  private File loadPgnFile(InputStream stream) {
    File file;
    try {
      file = new File(stream);
      file.loadNextGame();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return file;
  }

  public void newGame() {
    try {
      newGameRaw();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void newGameRaw() throws Exception {
    board.pieces.clear();
    board.tiles.clear();

    game = new ChessGame();
    game.board.tilesAll.forEach(
        t -> {
          Board.Tile tile = new Board.Tile();
          tile.ref = t;

          if (t.getRelative(EdgeType.LEFT).length == 0) {
            tile.gutter.put(EdgeType.LEFT, t.position.rowString());
          }
          if (t.getRelative(EdgeType.DOWN).length == 0) {
            tile.gutter.put(EdgeType.DOWN, t.position.colString());
          }
          if (t.getRelative(EdgeType.RIGHT).length == 0) {
            tile.gutter.put(EdgeType.RIGHT, t.position.rowString());
          }
          if (t.getRelative(EdgeType.UP).length == 0) {
            tile.gutter.put(EdgeType.UP, t.position.colString());
          }

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
  }

  private void replayGame(Game game) {
    newGame();
    replayMoves(game, 0, 0);
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
      System.out.println("No move for " + move.toString());
      return;
    } else if (movements.size() > 1) {
      System.out.println(move.toString() + " ambiguous (" + movements.size() + ")");
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
        (long) 200);
  }

  private BorderPane addMenuBar() {
    MenuBar menuBar = new MenuBar();

    MenuItem resetmi = new MenuItem("New Game");
    resetmi.setOnAction(event -> newGame());

    MenuItem copypgn = new MenuItem("Copy notation");
    copypgn.setOnAction(event -> copyNotation());

    MenuItem pastepgn = new MenuItem("Paste game from notation");
    pastepgn.setOnAction(
        event -> replayGameFromNotation(Clipboard.getSystemClipboard().getString()));

    playPgnMenu = new Menu("Play PGN");
    menuBar.getMenus().add(new Menu("Game", null, resetmi, playPgnMenu, copypgn, pastepgn));

    MenuItem mi = new MenuItem("Load demo games");
    playPgnMenu.getItems().add(mi);
    mi.setOnAction(
        event -> {
          InputStream resource = Gui.class.getResourceAsStream("carlsen-excerpt.pgn");
          pgnFile = loadPgnFile(resource);
          playPgnMenu.getItems().clear();
          for (Game g : pgnFile.games) addGame(g);
          try {
            while (pgnFile.loadNextGame()) {
              addGame(pgnFile.games.get(pgnFile.games.size() - 1));
            }
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });

    if (isOsx()) menuBar.useSystemMenuBarProperty().set(true);

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(menuBar);
    return borderPane;
  }

  private boolean isOsx() {
    final String os = System.getProperty("os.name");
    return os != null && os.startsWith("Mac");
  }

  private void addGame(Game g) {
    MenuItem mi = new MenuItem(g.getTitle());
    playPgnMenu.getItems().add(mi);
    mi.setOnAction(
        event -> {
          replayGame(g);
        });
  }

  public void copyNotation() {
    ClipboardContent content = new ClipboardContent();
    content.putString(game.getNotationString());
    Clipboard.getSystemClipboard().setContent(content);
  }

  public void replayGameFromNotation(String notation) {
    File file = loadPgnFile(new ByteArrayInputStream(notation.getBytes()));
    if (file.games.size() == 0) return;
    replayGame(file.games.get(0));
  }

  private boolean showForNotation(String command, boolean executeIfUnambiguous) {
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
        return true;
      }
    }
    return false;
  }

  private boolean playMovement(Movement m) {
    Optional<Board.Tile> source = board.getTile(m.edge.source);
    Optional<Board.Tile> t = board.getTile(m.edge.target);
    if (!source.isPresent()) return false;

    Board.Piece p = board.getPiece(source.get()).get();

    try {
      game.playMove(new Move(game.getCurrentPlayer(), m.edge.source.getPiece(), m));
    } catch (InvalidMoveException e) {
      return false;
    }

    if (m.getCapturedPiece().isPresent()) {
      board.getPiece(t.get()).ifPresent(Board.Piece::setCaptured);
    }

    if (m instanceof CastlingMovement) {
      Edge rookEdge = ((CastlingMovement) m).rookEdge;
      Board.Piece rook = board.getPiece(board.getTile(rookEdge.source).get()).get();
      rook.tile = board.getTile(rookEdge.target).get();
      rook.updateLayout();
    }

    p.tile = t.get();
    p.updateLayout();
    return true;
  }

  private String keySequence = "";

  private void addKeyboardInteraction(Group root) {
    root.setOnKeyTyped(
        event -> {
          if (event.getCharacter() != null
              && event.getCharacter().length() == 1
              && !event.getCharacter().equals("\r")) keySequence += event.getCharacter();
          if (showForNotation(keySequence, true)) keySequence = "";
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
            if (p.view.getBoundsInParent().contains(dragStart) && !p.isCaptured()) {
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
            board.view.getChildren().remove(dragPiece.view);
            board.view.getChildren().add(dragPiece.view);

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
    return playMovement(movement);
  }

  private void updateBoard() {
    Player p = game.getCurrentPlayer();
    Player p2 = Arrays.stream(game.getPlayers()).filter(l -> !l.equals(p)).findFirst().get();
    List<Movement> ms = p.getAvailableMovements(game.board);
    List<Movement> mo = p2.getAvailableMovements(game.board);
    updateBoardWithAvailableMovements(ms, mo);
  }

  private void updateBoardWithAvailableMovements(List<Movement> ms, List<Movement> others) {
    game.board.setTileMarksFromAvailableMovements(
        ms.toArray(new Movement[] {}), others.toArray(new Movement[] {}));
    board.tiles.forEach(
        t -> {
          t.setMarker(game.board.getMarkerForTile(t.ref));
        });
  }

  private void updateBoardWithAvailableMovements(List<Movement> ms) {
    updateBoardWithAvailableMovements(ms, new ArrayList<Movement>());
  }
}
