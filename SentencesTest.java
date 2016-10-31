import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.inference.OptimizedDPLL;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.kb.data.Clause;

public class SentencesTest {
    public static void main (String[] args) {
        Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();
        Literal aa = new Literal(new PropositionSymbol("A"));
        Literal bb = new Literal(new PropositionSymbol("B"));
        Literal cc = new Literal(new PropositionSymbol("C"));
        Literal dd = new Literal(new PropositionSymbol("D"));
        ComplexSentence complex = new ComplexSentence(Connective.OR, aa, bb);
        ComplexSentence complex2 = new ComplexSentence(Connective.AND, cc, dd);
        ComplexSentence complex3 = new ComplexSentence(Connective.OR, complex, complex2);
        DPLLSatisfiable dpls = new DPLLSatisfiable();

        Scanner sc = new Scanner(System.in);
        values.put(aa, true);
        values.put(bb, false);
        values.put(cc, false);

        Model model = new Model(values);
        System.out.println("Model dpll satisfiable : " + model.isTrue(complex));

        System.out.println("Model getValue: " + model.getValue(aa));
        System.out.println("Model isTrue: " + model.isTrue(complex));
        System.out.println("Complex sentence is and sentence: " + complex.isAndSentence());

        int dimension = Integer.parseInt(sc.nextLine());
        SudokuLaboratory sl = new SudokuLaboratory(dimension);
        System.out.println(sl.symbols);
        System.out.println("Symbols count: " + sl.symbols.size());

        Sentence testDisj = complex.newDisjunction(sl.getSymbolsInRow(1));
        Sentence testDisj2 = complex.newDisjunction(aa, bb);
        System.out.println("Value of sentence testDisj2 to model is: " + model.isTrue(testDisj2));
        // String symvals = sc.nextLine();
        sl.assignValuesInOrder(1);
        Model newModel = sl.model;
        System.out.println("Value of sentence acc. to model is: " + newModel.isTrue(testDisj));
    }
}

class SudokuLaboratory {
    final int dimension;
    final ArrayList<PropositionSymbol> symbols;
    Model model;

    public SudokuLaboratory(int dimension) {
        this.dimension = dimension;
        this.symbols = generateSymbols();
        this.model = new Model();
    }

    public void assignValuesInOrder(int row) {
        Scanner sc = new Scanner(System.in);
        Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();
        for (PropositionSymbol symbol : symbols) {
            System.out.print(symbol.getSymbol() + ": ");
            if (sc.nextLine().equals("true")) {
                values.put(symbol, true);
            }
        }
        this.model = new Model(values);
    }

    private ArrayList<PropositionSymbol> generateSymbols() {
        ArrayList<PropositionSymbol> generated = new ArrayList<PropositionSymbol>();
        for (int x=1; x<=dimension; x++) {
            for (int y=1; y<=dimension; y++) {
                for (int z=1; z<=dimension; z++) {
                    generated.add(new PropositionSymbol("x"+x+"y"+y+"z"+z));
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
