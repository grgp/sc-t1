import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.framework.qsearch.GraphSearch;

public class Tugas1A {
  public static void main(String[] args) {
    String strategy = args[0];

    int rows = 0, cols = 0, t_initial_row = 0, t_initial_col = 0;
    String tmp[], item_locations[], obst_locations[];

    try {

    	Scanner input = new Scanner(System.in);
        tmp = input.nextLine().split(",");
        rows = Integer.parseInt(tmp[0]);
        cols = Integer.parseInt(tmp[1]);
        tmp = input.nextLine().split(",");
        t_initial_row = Integer.parseInt(tmp[0]);
        t_initial_col = Integer.parseInt(tmp[1]);
        item_locations = input.nextLine().split(" ");
        obst_locations = input.nextLine().split(" ");
        input.close();

        Set<Point> initial_items = new HashSet<Point>();

        for (String it : item_locations) {
            it = it.substring(1, it.length()-1);
            String[] ita = it.split(",");
            initial_items.add(new Point(Integer.parseInt(ita[0]), Integer.parseInt(ita[1])));
        }

        Set<Point> obstacles = new HashSet<Point>();

        for (String it : obst_locations) {
            it = it.substring(1, it.length()-1);
            String[] ita = it.split(",");
            obstacles.add(new Point(Integer.parseInt(ita[0]), Integer.parseInt(ita[1])));
        }

        Point t_initial_location = new Point(t_initial_row, t_initial_col);
        State newState = new State(rows, cols, initial_items, obstacles, t_initial_location);

        JarvisActionsFunction actionsFunction = new JarvisActionsFunction();
        JarvisResultFunction resultFunction = new JarvisResultFunction();
        JarvisGoalTest goalTest = new JarvisGoalTest();

        System.out.println("Starting search");
        Problem problem = new Problem(newState, actionsFunction, resultFunction, goalTest);
        List<Action> listOfActions = new ArrayList<Action>();

        if (strategy.equals("ids")) {
            IterativeDeepeningSearch ids = new IterativeDeepeningSearch();
            listOfActions = ids.search(problem);
        } else if (strategy.equals("dls")) {
            DepthLimitedSearch dls = new DepthLimitedSearch(3);
            listOfActions = dls.search(problem);
        } else if (strategy.equals("a*")) {
            GraphSearch impl = new GraphSearch();
            AStarSearch astar = new AStarSearch(impl, new JarvisHeuristicFunction());
            listOfActions = astar.search(problem);
        }

        for (Action action : listOfActions) {
            if (action instanceof ActionTony) {
              ActionTony at = (ActionTony) action;
              System.out.println(at.direction);
            }
        }

    } catch (Exception ex) {
	    ex.printStackTrace();
        System.exit(0);
	}

  }
}

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
        System.out.println("min distance from " + t_location + " is: " + min + " on point " + pt);
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

class JarvisActionsFunction implements ActionsFunction {
    State currentState;

    public Set<Action> actions(Object s) {
        if (s instanceof State) {
            currentState = (State) s;
            Set<Action> possibleActions = new HashSet<Action>();

            if (currentState.items.contains(currentState.t_location)) {
                System.out.println("                        >>> AMBIL");
                possibleActions.add(new ActionTony("AMBIL"));
            }
            if (moveValid(currentState, "ATAS")) {
                possibleActions.add(new ActionTony("ATAS"));
            }
            if (moveValid(currentState, "KANAN")) {
                possibleActions.add(new ActionTony("KANAN"));
            }
            if (moveValid(currentState, "BAWAH")) {
                possibleActions.add(new ActionTony("BAWAH"));
            }
            if (moveValid(currentState, "KIRI")) {
                possibleActions.add(new ActionTony("KIRI"));
            }

            return possibleActions;
        } else {
            return null;
        }
    }

    private boolean moveValid(State currentState, String direction) {
        // System.out.println("From: " + currentState.t_location.x + " " + currentState.t_location.y + " | dir: " + direction);
        int checkX = currentState.t_location.x + getXOffset(direction);
        int checkY = currentState.t_location.y + getYOffset(direction);
        if ( inRange("x-axis", checkX) &&
             inRange("y-axis", checkY) ) {
            Point tempPoint = new Point(checkX, checkY);
            if ( !currentState.obstacles.contains(tempPoint) ) {
               System.out.println("From: " + currentState.t_location.x + " " + currentState.t_location.y);
               System.out.println("To: " + tempPoint.x + " " + tempPoint.y + " | dir: " + direction);
               System.out.println("                        >>> " + direction);
               return true;
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean inRange(String axis, int index) {
        if (axis.equals("x-axis")) {
            return (index <= currentState.rows) && (index > 0);
        } else if (axis.equals("y-axis")) {
            return (index <= currentState.cols) && (index > 0);
        } else {
            return false;
        }
    }

    private int getXOffset(String direction) {
        if (direction.equals("ATAS")) {
            return -1;
        } else if (direction.equals("BAWAH")) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getYOffset(String direction) {
      if (direction.equals("KIRI")) {
          return -1;
      } else if (direction.equals("KANAN")) {
          return 1;
      } else {
          return 0;
      }
    }
}

class JarvisResultFunction implements ResultFunction {
  public Object result(Object s, Action a) {
      if (s instanceof State && a instanceof ActionTony) {
          State state = (State) s;
          ActionTony newAction = (ActionTony) a;
          Object newState = state.updateState(newAction);
          return newState;
      } else {
          return s;
      }
  }
}

class JarvisGoalTest implements GoalTest {
    public boolean isGoalState(Object s) {
        if (s instanceof State) {
            State state = (State) s;
            return state.items.isEmpty();
        } else {
            return false;
        }
    }
}

class JarvisStepCostFunction implements StepCostFunction {
    public double c(Object s, Action a, Object sDelta) {
        if (a instanceof ActionTony) {
            ActionTony at = (ActionTony) a;
            if (at.direction.equals("AMBIL")) {
                return 0;
            } else {
                return 1.0;
            }
        }
        return 0;
    }
}

class JarvisHeuristicFunction implements HeuristicFunction {
    public double h (Object s) {
        if (s instanceof State) {
            State state = (State) s;
            return state.getShortestItemDistance();
        } else {
            return 0;
        }
    }
}
