import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Set;
import java.util.List;

import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.inference.OptimizedDPLL;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.kb.data.Clause;

import aima.core.logic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.logic.propositional.inference.WalkSAT;
import aima.core.logic.propositional.kb.data.ConjunctionOfClauses;
import java.util.Random;

public class Tugas1B_it1 {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            //sc.skip("[\r\n]+");
            int dimension = Integer.parseInt(sc.nextLine());

            SudokuFactory sf = new SudokuFactory(dimension);
            // System.out.println(sf.symbols);
            System.out.println("Symbols count: "+ sf.symbols.size());

            int[][] data = new int[dimension][dimension];
            Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();

            for (int row = 1; row <= dimension; row++) {
                for (int col = 1; col <= dimension; col++) {
                    int val = sc.nextInt();
                    String key = "x"+row+"y"+col+"z"+val;
                    if (val >= 1 && val <= dimension) {
                        values.put(sf.symbols.get(key), true);
                    } else if (val == 0) {
                        values.put(sf.symbols.get(key), false);
                    }
                }
            }

            sf.model = new Model(values);
            // System.out.println(sf.cellSatifiesSudoku());
            System.out.println("initial model: \n"+ sf.model);

            ConvertToConjunctionOfClauses ctcc = new ConvertToConjunctionOfClauses();
            ConjunctionOfClauses coc = ctcc.convert(sf.cellSatifiesSudoku());
            //System.out.println("\n-----\nConjunction of Clauses:\n"+coc);
            WalkSATModified ws = new WalkSATModified();
            // System.out.println("");
            Model newerModel = ws.walkSAT(coc.getClauses(), 0.5, -1, sf.model);

            OptimizedDPLL odp = new OptimizedDPLL();

            // System.out.println("\nnewerModel: \n\n"+newerModel);


        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}

class WalkSATModified extends WalkSAT {
    public Model walkSAT(Set<Clause> clauses, double p, int maxFlips, Model model) {
		assertLegalProbability(p);

		// for i = 1 to max_flips do (Note: maxFlips < 0 means infinity)
        // model = randomAssignmentToSymbolsInClauses(clauses);
        // System.out.println("random model: " + model);
		for (int i = 0; i < maxFlips || maxFlips < 0; i++) {
			// if model satisfies clauses then return model
			if (model.satisfies(clauses)) {
                System.out.println("yes it actually satisfies");
				return model;
			}

			// clause <- a randomly selected clause from clauses that is false
			// in model
			Clause clause = randomlySelectFalseClause(clauses, model);

			// with probability p flip the value in model of a randomly selected
			// symbol from clause
			if (random.nextDouble() < p) {
				model = model.flip(randomlySelectSymbolFromClause(clause));
			} else {
				// else flip whichever symbol in clause maximizes the number of
				// satisfied clauses
				model = flipSymbolInClauseMaximizesNumberSatisfiedClauses(
						clause, clauses, model);
			}
		}

		// return failure
		return null;
	}

    private Random random = new Random();

    protected Clause randomlySelectFalseClause(Set<Clause> clauses, Model model) {
		// Collect the clauses that are false in the model
        // System.out.println("clauses size: "+clauses.size());
        // System.out.println("----\nClauses in randomly:\n"+clauses);
		List<Clause> falseClauses = new ArrayList<Clause>();
		for (Clause c : clauses) {
			if (Boolean.FALSE.equals(model.determineValue(c))) {
				falseClauses.add(c);
                System.out.println("---false: " + c + " ---");
			} else {
                System.out.println("---true : " + c + " ---");
            }
		}
        System.out.println("falsecla size: "+falseClauses.size());

		// a randomly selected clause from clauses that is false
		Clause result = falseClauses.get(random.nextInt(falseClauses.size()));
		return result;
	}

}
