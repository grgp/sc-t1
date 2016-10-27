import java.util.Set;
import java.util.HashSet;
import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.GoalTest;

public class Tugas1A_factories {

}

class JarvisActionsFunction implements ActionsFunction {
    State currentState;

    public Set<Action> actions(Object s) {
        if (s instanceof State) {
            currentState = (State) s;
            Set<Action> possibleActions = new HashSet<Action>();

            if (currentState.items.contains(currentState.t_location)) {
                // System.out.println("TAKE");
                possibleActions.add(new ActionTony("TAKE"));
            }
            else if (moveValid(currentState, "UP")) {
                // System.out.println("UP");
                possibleActions.add(new ActionTony("UP"));
            }
            else if (moveValid(currentState, "RIGHT")) {
                // System.out.println("RIGHT");
                possibleActions.add(new ActionTony("RIGHT"));
            }
            else if (moveValid(currentState, "DOWN")) {
                // System.out.println("DOWN");
                possibleActions.add(new ActionTony("DOWN"));
            }
            else if (moveValid(currentState, "LEFT")) {
                // System.out.println("LEFT");
                possibleActions.add(new ActionTony("LEFT"));
            }

            return possibleActions;
        } else {
            return null;
        }
    }

    private boolean moveValid(State currentState, String direction) {
        System.out.println("From: " + currentState.t_location.x + " " + currentState.t_location.y);
        int checkX = currentState.t_location.x + getXOffset(direction);
        int checkY = currentState.t_location.y + getYOffset(direction);
        if ( inRange("x-axis", checkX) &&
             inRange("y-axis", checkY) ) {
            Point tempPoint = new Point(checkX, checkY);
            if ( !currentState.obstacles.contains(tempPoint) ) {
               System.out.println("To: " + tempPoint.x + " " + tempPoint.y);
               System.out.println(direction);
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
        if (direction.equals("UP")) {
            return -1;
        } else if (direction.equals("DOWN")) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getYOffset(String direction) {
      if (direction.equals("LEFT")) {
          return -1;
      } else if (direction.equals("RIGHT")) {
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
          state.updateState(a);
          return state;
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
