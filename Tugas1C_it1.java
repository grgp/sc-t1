import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.List;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Model;

import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.inference.OptimizedDPLL;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.kb.data.Clause;

import aima.core.logic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.logic.propositional.inference.WalkSAT;
import aima.core.logic.propositional.kb.data.ConjunctionOfClauses;
import java.util.Random;

public class Tugas1C_it1 {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            int dimension = Integer.parseInt(sc.nextLine());

            SudokuFactory sf = new SudokuFactory(dimension);
            System.out.println("KB after initialize: \n" + sf.kb);

            String ns = "";
            for (int row = 1; row <= dimension; row++) {
                for (int col = 1; col <= dimension; col++) {
                    int val = sc.nextInt();
                    String key = "x"+row+"y"+col+"z"+val;
                    if (val >= 1 && val <= dimension) {
                        ns += key + " ";
                        sf.kb.tell(sf.symbols.get(key));
                    }
                }
                ns += "\n";
            }

            System.out.println("KB after input: \n" + sf.kb);
            System.out.println("ns: \n" + ns);

            WalkSAT ws = new WalkSAT();
            Model model = ws.walkSAT(sf.kb.asCNF(), 0.5, -1);
            System.out.println("walkSAT: ");
            printSudokuModel(sf, model);

            OptimizedDPLL odp = new OptimizedDPLL();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private static void printSudokuModel(SudokuFactory sf, Model model) {
        for (int row = 1; row <= sf.dimension; row++) {
            for (int col = 1; col <= sf.dimension; col++) {
                for (int val = 1; val <= sf.dimension; val++) {
                    String key = "x"+row+"y"+col+"z"+val;
                    if (model.getValue(sf.symbols.get(key))) {
                        System.out.print(key + " ");
                    }
                }
            }
            System.out.println();
        }
    }
}
