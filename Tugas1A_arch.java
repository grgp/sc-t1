import java.util.Scanner;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.lang.Math;
import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.uninformed.IterativeDeepeningSearch;

class State {
    final int rows;
    final int cols;
    final Set<Point> obstacles;
    final Set<Point> items;
    final Point t_location;

    public State(int rows, int cols, Set<Point> items, Set<Point> obstacles,
                 Point t_location) {
        this.rows = rows;
        this.cols = cols;
        this.obstacles = obstacles;
        this.items = items;
        this.t_location = t_location;
    }

    public Object updateState(Action a) {
        if (a instanceof ActionTony) {
            ActionTony nextAction = (ActionTony) a;
            Set<Point> newItems = new HashSet<Point>(items);
            Point newLocation = new Point(t_location);

            if (nextAction.direction.equals("AMBIL")) {
                newItems.remove(this.t_location);
            } else {
                newLocation.updatePoint(nextAction);
            }

            State newState = new State(rows, cols, newItems, obstacles, newLocation);
            return newState;
        } return null;
    }

    public double getShortestItemDistance() {
        double min = Integer.MAX_VALUE;
        Point pt = null;
        for (Point p2 : items) {
            double tmp_distance = getManhattanDistance(p2);
            if (tmp_distance < min) {
                pt = p2;
                min = tmp_distance;
            }
        }
        return min;
    }

    // get the shortest manhattan distance between
    //     current location and all other items
    public double getManhattanDistance(Point p2) {
        return Math.abs(t_location.x-p2.x) + Math.abs(t_location.y-p2.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof State) {
            State o = (State) obj;
            return o.items.equals(this.items);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, cols, obstacles, items, t_location);
    }

}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void updatePoint(ActionTony a) {
        this.x += a.offsetX;
        this.y += a.offsetY;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point o = (Point) obj;
            return (this.x == o.x) && (this.y == o.y);
        } else {
            return false;
        }
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}

class Distance extends Point {
    public Distance(int x, int y) {
        super(x, y);
    }
}

class ActionTony implements Action {
    String direction;
    int offsetX;
    int offsetY;

    public ActionTony(String direction) {
        this.direction = direction;
        offsetX = 0; offsetY = 0;
        if (direction.equals("ATAS")) offsetX = -1;
        else if (direction.equals("KANAN")) offsetY = 1;
        else if (direction.equals("BAWAH")) offsetX = 1;
        else if (direction.equals("KIRI")) offsetY = -1;
    }

    public boolean isNoOp() {
        return false;
    }
}
