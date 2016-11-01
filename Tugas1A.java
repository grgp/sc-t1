/**
 * @author: George Albert
 * @author_npm: 1406569781
 * @class: Sistem Cerdas A
 *
 * Class containing the main program to read the input, construct the
 * problem definition, execute a search then finally outputs it to a file.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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

    // avoid exceptions caused by I/O, shouldn't be a problem if
    //     the input is valid
    try {
        // initialize I/O
        Scanner input = null;
        PrintWriter pw = null;
        if (args.length >= 3) {
            File input_file = new File(args[1]);
            input = new Scanner(input_file);

            File output_file = new File(args[2]);
            FileWriter fw = new FileWriter(output_file);
            pw = new PrintWriter(fw);
        } else {
            System.out.println("Error: Missing arguments for program");
            System.exit(0);
        }

        // parse data from the input text
        tmp = input.nextLine().split(",");
        rows = Integer.parseInt(tmp[0]);
        cols = Integer.parseInt(tmp[1]);
        tmp = input.nextLine().split(",");
        t_initial_row = Integer.parseInt(tmp[0]);
        t_initial_col = Integer.parseInt(tmp[1]);
        item_locations = input.nextLine().split(" ");
        obst_locations = input.nextLine().split(" ");
        input.close();

        // create a Set containing the locations of items
        Set<Point> initial_items = new HashSet<Point>();
        for (String it : item_locations) {
            it = it.substring(1, it.length()-1);
            String[] ita = it.split(",");
            initial_items.add(new Point(Integer.parseInt(ita[0]), Integer.parseInt(ita[1])));
        }

        // create a Set containing the locations of obstacles
        Set<Point> obstacles = new HashSet<Point>();
        for (String it : obst_locations) {
            it = it.substring(1, it.length()-1);
            String[] ita = it.split(",");
            obstacles.add(new Point(Integer.parseInt(ita[0]), Integer.parseInt(ita[1])));
        }

        // construct the problem definition by initializing the relevant values
        Point t_initial_location = new Point(t_initial_row, t_initial_col);
        State newState = new State(rows, cols, initial_items, obstacles, t_initial_location);

        JarvisActionsFunction actionsFunction = new JarvisActionsFunction();
        JarvisResultFunction resultFunction = new JarvisResultFunction();
        JarvisGoalTest goalTest = new JarvisGoalTest();
        JarvisStepCostFunction stepCostFunction = new JarvisStepCostFunction();

        Problem problem = new Problem(newState, actionsFunction, resultFunction, goalTest, stepCostFunction);
        List<Action> listOfActions = new ArrayList<Action>();

        // execute search according to the selected strategy
        if (strategy.equals("ids")) {
            IterativeDeepeningSearch ids = new IterativeDeepeningSearch();
            listOfActions = ids.search(problem);
            pw.println((int) ids.getMetrics().getDouble("pathCost"));
            pw.println(ids.getMetrics().getInt("nodesExpanded"));
        } else if (strategy.equals("dls")) {
            DepthLimitedSearch dls = new DepthLimitedSearch(3);
            listOfActions = dls.search(problem);
            pw.println((int) dls.getMetrics().getDouble("pathCost"));
            pw.println(dls.getMetrics().getInt("nodesExpanded"));
        } else if (strategy.equals("a*")) {
            GraphSearch impl = new GraphSearch();
            AStarSearch astar = new AStarSearch(impl, new JarvisHeuristicFunction());
            listOfActions = astar.search(problem);
            pw.println((int) astar.getMetrics().getDouble("pathCost"));
            pw.println(astar.getMetrics().getInt("nodesExpanded"));
        }

        // print the correct list of actions retrieved by search
        for (Action action : listOfActions) {
            if (action instanceof ActionTony) {
              ActionTony at = (ActionTony) action;
              pw.println(at.direction);
            }
        }

        pw.close();

    // catch exceptions (mostly I/O related), exit when caught
    } catch (Exception ex) {
        ex.printStackTrace();
        System.exit(0);
    }

    }
}
