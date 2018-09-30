package com.leonardpauli.experiments.boardgame.chess.gui;

import com.leonardpauli.experiments.boardgame.board.tile.EdgeType;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.lang.Double.min;
import static java.lang.Math.round;

public class Board {
  public List<Tile> tiles = new ArrayList<>();
  public List<Piece> pieces = new ArrayList<>();
  public Group view = new Group();
  public Point2D origin;
  public Point2D size;

  public void resetView() {
    ObservableList<Node> views = view.getChildren();
    views.clear();
    tiles.forEach(t -> views.add(t.view));
    pieces.forEach(t -> views.add(t.view));
  }

  public Optional<Board.Tile> getTileAtPosition(Point2D pos) {
    for (Board.Tile t : tiles) {
      if (t.view.getBoundsInParent().contains(pos)) {
        return Optional.of(t);
      }
    }
    return Optional.empty();
  }

  public Optional<Board.Tile> getTile(com.leonardpauli.experiments.boardgame.board.tile.Tile ref) {
    for (Board.Tile t : tiles) {
      if (t.ref.equals(ref)) {
        return Optional.of(t);
      }
    }
    return Optional.empty();
  }

  public Optional<Board.Piece> getPiece(Tile tile) {
    for (Board.Piece p : pieces) {
      if (p.tile == tile && !p.isCaptured) {
        return Optional.of(p);
      }
    }
    return Optional.empty();
  }

  public static class Tile {
    public List<Point2D> corners = new ArrayList<>();
    public Color baseColor = Color.hsb(0, 0, 0.85);
    public Group view = new Group();
    public Path bg = new Path();
    public com.leonardpauli.experiments.boardgame.board.tile.Tile ref;
    public HashMap<EdgeType, String> gutter = new HashMap<>();
    private HashMap<EdgeType, Text> gutterViews = new HashMap<>();

    public Tile() {
      view.getChildren().add(bg);
      bg.setStrokeWidth(0);
    }

    public void updateBgPath(Board board, double factor) {
      Util.simplePathInterpolation(bg, corners, board.size, board.origin, factor);
      updateGutterViews();
    }

    private void updateGutterViews() {
      double fontSize = 10;
      double margin = fontSize - 1;
      for (EdgeType t : EdgeType.values()) {
        String val = gutter.get(t);
        Text v = gutterViews.get(t);
        if (val == null) {
          if (v != null) {
            gutterViews.remove(t);
            view.getChildren().remove(v);
          }
          continue;
        }
        if (v == null) {
          v = new Text();
          v.setFill(Color.gray(1));
          v.setTextAlignment(TextAlignment.CENTER);
          v.setTextOrigin(VPos.CENTER);
          v.setFont(Font.font(null, FontWeight.EXTRA_BOLD, fontSize));
          v.setOpacity(0.2);

          gutterViews.put(t, v);
          view.getChildren().add(v);
        }
        v.setText(val);
        v.setX(
            bg.getBoundsInParent().getMinX()
                + bg.getBoundsInParent().getWidth()
                    * (t == EdgeType.DOWN || t == EdgeType.UP ? 0.5 : t == EdgeType.RIGHT ? 1 : 0)
                + (t == EdgeType.RIGHT
                    ? -fontSize * 0.4 + margin
                    : t == EdgeType.LEFT ? -fontSize * 0.4 - margin : -fontSize / 2));
        v.setY(
            bg.getBoundsInParent().getMinY()
                + bg.getBoundsInParent().getHeight()
                    * (t == EdgeType.DOWN ? 1 : t == EdgeType.RIGHT || t == EdgeType.LEFT ? 0.5 : 0)
                + (t == EdgeType.UP ? -margin : t == EdgeType.DOWN ? margin : 0));
      }
    }

    public void updateBgColor() {
      bg.setFill(getCurrentColor());
    }

    public Bounds getBounds() {
      return bg.getBoundsInParent();
    }

    public Color getCurrentColor() {
      return baseColor
          .interpolate(Color.hsb(20, 0.0, 0.2), 0.85)
          .interpolate(Color.hsb(20, 1.0, 0.5), 0.05)
          .darker()
          .darker()
          .darker();
    }

    public void setMarked(boolean marked) {
      bg.setFill(marked ? getCurrentColor().deriveColor(80, 2, 1.1, 1) : getCurrentColor());
    }
  }

  public static class Piece {
    public Text view = new Text("♞");
    public Tile tile;
    public Point2D position;
    public com.leonardpauli.experiments.boardgame.actor.Piece ref;
    private boolean isCaptured = false;

    Piece() {
      view.setTextOrigin(VPos.CENTER);
      view.setTextAlignment(TextAlignment.LEFT);
    }

    public void updateLayout(Bounds bounds) {
      double fontSize = round(min(bounds.getWidth(), bounds.getHeight()) * 0.7);
      if (isCaptured) fontSize *= 0.3;
      view.setFont(Font.font(fontSize));
      position = new Point2D(bounds.getMinX(), bounds.getMinY());

      if (isCaptured)
        position =
            position.add(
                new Point2D(bounds.getWidth() * 0.05, bounds.getWidth() * 0.05 + fontSize * 0.47));
      else
        position =
            position.add(
                new Point2D((bounds.getWidth() - fontSize) / 2, bounds.getHeight() * 0.47));

      view.setX(position.getX());
      view.setY(position.getY());
    }

    public void updateLayout() {
      updateLayout(tile.getBounds());
    }

    public void updatePosition(Point2D pos) {
      view.setX(pos.getX());
      view.setY(pos.getY());
    }

    public void setCaptured() {
      isCaptured = true;
      view.setOpacity(0.5);
      updateLayout();
    }

    public boolean isCaptured() {
      return isCaptured;
    }
  }
}
