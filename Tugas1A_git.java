import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.uninformed.IterativeDeepeningSearch;

public class Tugas1A_git {
  public static void main(String[] args) {
    //String strategy = args[0];

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

    System.out.println("Before adding items and obstacles");

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

    System.out.println("Adding obstacles");

    Point t_initial_location = new Point(t_initial_row, t_initial_col);
    State newState = new State(rows, cols, initial_items, obstacles, t_initial_location);

    ActionsFunction actionsFunction = new ActionsFunction(){
        public Set<Action> actions(Object s) {
            if (s instanceof State) {
                State currentState = (State) s;
                Set<Action> possibleActions = new HashSet<Action>();
                possibleActions.add(new ActionTony("UP"));
                possibleActions.add(new ActionTony("RIGHT"));
                possibleActions.add(new ActionTony("DOWN"));
                possibleActions.add(new ActionTony("LEFT"));
                return possibleActions;
            } else {
                return null;
            }
        }
    };

    ResultFunction resultFunction = new ResultFunction(){
        public Object result(Object s, Action a) {
            if (s instanceof State && a instanceof ActionTony) {
                State state = (State) s;
                state.updateState(a);
                return state;
            } else {
                return s;
            }
        }
    };

    GoalTest goalTest = new GoalTest(){
        public boolean isGoalState(Object s) {
            if (s instanceof State) {
                State state = (State) s;
                return state.items.isEmpty();
            } else {
                return false;
            }
        }
    };

    Problem problem = new Problem(newState, actionsFunction, resultFunction, goalTest);

    System.out.println("Starting search");

    IterativeDeepeningSearch ids = new IterativeDeepeningSearch();
    ids.search(problem);

    System.out.println("Reached the end bb");

    } catch (Exception ex) {
		    ex.printStackTrace();
        System.exit(0);
	  }

  }
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
            Point tempPoint = new Point(t_location.x, t_location.y);
            tempPoint.updatePoint(nextAction.getXOffset(), nextAction.getYOffset());
            if ( !this.obstacles.contains(tempPoint) ) {
                t_location = tempPoint;
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

    public void updatePoint(int x, int y) {
        this.x += x;
        this.y += y;
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

  public ActionTony(String direction) {
      this.direction = direction;
  }

  public boolean isNoOp() {
      return false;
  }

  public int getXOffset() {
      if (direction.equals("UP")) {
          return -1;
      } else if (direction.equals("DOWN")) {
          return 1;
      } else {
          return 0;
      }
  }

  public int getYOffset() {
    if (direction.equals("LEFT")) {
        return -1;
    } else if (direction.equals("RIGHT")) {
        return 1;
    } else {
        return 0;
    }
  }
}
