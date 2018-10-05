package com.leonardpauli.experiments.boardgame.chess.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import java.util.List;

import static java.lang.Double.max;

public class Util {
  static void simplePathInterpolation(
      Path path, List<Point2D> newPoints, Point2D scale, Point2D offset, double factor) {
    ObservableList<PathElement> points = path.getElements();
    int pointsCount = points.size();
    int newPointsCount = newPoints.size();

    Point2D lastTarget = null;
    for (int i = 0; i < max(newPointsCount, pointsCount); i++) {
      Point2D target = lastTarget != null ? lastTarget.multiply(1) : new Point2D(0, 0);
      if (i < newPointsCount) {
        target = newPoints.get(i);
        target = new Point2D(target.getX() * scale.getX(), target.getY() * scale.getY());
        target = target.add(offset);
      }
      if (lastTarget == null) lastTarget = target.multiply(1);

      Point2D before = lastTarget.multiply(1);
      lastTarget = target.multiply(1);
      if (i < pointsCount) {
        PathElement pe = points.get(i);
        if (pe instanceof LineTo) {
          before = new Point2D(((LineTo) pe).getX(), ((LineTo) pe).getY());
        } else if (pe instanceof MoveTo) {
          before = new Point2D(((MoveTo) pe).getX(), ((MoveTo) pe).getY());
        }
      }

      Point2D dist = target.subtract(before);
      Point2D next = before.add(dist.multiply(factor));

      if (i < pointsCount) {
        PathElement pe = points.get(i);
        if (pe instanceof LineTo) {
          ((LineTo) pe).setX(next.getX());
          ((LineTo) pe).setY(next.getY());
        } else if (pe instanceof MoveTo) {
          ((MoveTo) pe).setX(next.getX());
          ((MoveTo) pe).setY(next.getY());
        }

        if (i >= newPointsCount && dist.magnitude() < 0.01) {
          points.remove(i);
          i--;
        }

      } else {
        points.add(
            i == 0 ? new MoveTo(next.getX(), next.getY()) : new LineTo(next.getX(), next.getY()));
      }
    }
  }
}
