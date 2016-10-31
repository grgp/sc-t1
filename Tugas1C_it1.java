import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.List;

import aima.core.logic.propositional.kb.KnowledgeBase;

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
            System.out.println("Symbols count: "+ sf.symbols.size());

            for (int row = 1; row <= dimension; row++) {
                for (int col = 1; col <= dimension; col++) {
                    int val = sc.nextInt();
                    String key = "x"+row+"y"+col+"z"+val;
                    if (val >= 1 && val <= dimension) {
                        sf.kb.tell(sf.symbols.get(key));
                    }
                }
            }

            ConvertToConjunctionOfClauses ctcc = new ConvertToConjunctionOfClauses();
            ConjunctionOfClauses coc = ctcc.convert(sf.cellSatifiesSudoku());

            OptimizedDPLL odp = new OptimizedDPLL();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
