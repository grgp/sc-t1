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

public class Tugas1A_git {
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
