import java.util.Set;
import java.util.HashSet;
import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.StepCostFunction;

import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.qsearch.QueueSearch;
import aima.core.search.framework.Node;
import java.util.Queue;
import java.util.PriorityQueue;

class JarvisActionsFunction implements ActionsFunction {
    State currentState;

    public Set<Action> actions(Object s) {
        if (s instanceof State) {
            currentState = (State) s;
            Set<Action> possibleActions = new HashSet<Action>();

            if (currentState.items.contains(currentState.t_location)) {
                // System.out.println("                        >>> AMBIL");
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
        int checkX = currentState.t_location.x + getXOffset(direction);
        int checkY = currentState.t_location.y + getYOffset(direction);
        if ( inRange("x-axis", checkX) &&
             inRange("y-axis", checkY) ) {
            Point tempPoint = new Point(checkX, checkY);
            if ( !currentState.obstacles.contains(tempPoint) ) {
            //    System.out.println("From: " + currentState.t_location.x + " " + currentState.t_location.y);
            //    System.out.println("To: " + tempPoint.x + " " + tempPoint.y + " | dir: " + direction);
            //    System.out.println("                        >>> " + direction);
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
