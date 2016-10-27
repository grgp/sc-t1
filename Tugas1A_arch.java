import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.uninformed.IterativeDeepeningSearch;

public class Tugas1A_arch {

}

class State {
    int rows;
    int cols;
    Set<Point> items;
    Set<Point> obstacles;
    Point t_location;

    public State(int rows, int cols, Set<Point> items, Set<Point> obstacles, Point t_location) {
        this.rows = rows;
        this.cols = cols;
        this.items = items;
        this.obstacles = obstacles;
        this.t_location = t_location;
    }

    public void updateState(Action a) {
        if (a instanceof ActionTony) {
            ActionTony nextAction = (ActionTony) a;
            if (nextAction == null) {
                System.out.println("Shouldn't happen");
            } else if (nextAction.direction.equals("TAKE")) {
                this.items.remove(this.t_location);
            } else {
                this.t_location.updatePoint(nextAction);
            }
        }
    }

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

}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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

}

class ActionTony implements Action {
    String direction;
    int offsetX;
    int offsetY;

    public ActionTony(String direction) {
        this.direction = direction;
        offsetX = 0; offsetY = 0;
        if (direction.equals("UP")) offsetX = -1;
        else if (direction.equals("RIGHT")) offsetY = 1;
        else if (direction.equals("DOWN")) offsetX = 1;
        else if (direction.equals("LEFT")) offsetY = -1;
    }

    public boolean isNoOp() {
        return false;
    }
}
