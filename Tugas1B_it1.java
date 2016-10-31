import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

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

public class Tugas1B_it1 {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            //sc.skip("[\r\n]+");
            int dimension = Integer.parseInt(sc.nextLine());

            SudokuFactory sf = new SudokuFactory(dimension);
            System.out.println(sf.symbols);
            System.out.println("Symbols count: "+ sf.symbols.size());

            int[][] data = new int[dimension][dimension];
            Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();

            for (int row = 1; row <= dimension; row++) {
                for (int col = 1; col <= dimension; col++) {
                    int val = sc.nextInt();
                    if (val >= 1 && val <= 9) {
                        String key = "x"+row+"y"+col+"z"+val;
                        values.put(sf.symbols.get(key), true);
                    }
                }
            }

            Model model = new Model(values);
            System.out.println(model);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}

class SudokuFactory {
    final int dimension;
    final HashMap<String, PropositionSymbol> symbols;
    Model model;

    public SudokuFactory(int dimension) {
        this.dimension = dimension;
        this.symbols = generateSymbols();
        this.model = new Model();
    }

    private HashMap<String, PropositionSymbol> generateSymbols() {
        HashMap<String, PropositionSymbol> generated = new HashMap<String, PropositionSymbol>();
        for (int x=1; x<=dimension; x++) {
            for (int y=1; y<=dimension; y++) {
                for (int z=1; z<=dimension; z++) {
                    generated.put("x"+x+"y"+y+"z"+z, new PropositionSymbol("x"+x+"y"+y+"z"+z));
                }
            }
        }
        return generated;
    }


    public ArrayList<PropositionSymbol> getSymbolsInRow(int row) {
        char selected_row = ("" + row).charAt(0);
        ArrayList<PropositionSymbol> row_symbols = new ArrayList<PropositionSymbol>();
        for (PropositionSymbol symbol : row_symbols) {
            if (symbol.getSymbol().charAt(1) == selected_row) {
                row_symbols.add(symbol);
            }
        }
        return row_symbols;
    }

}
