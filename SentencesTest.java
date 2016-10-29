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

public class SentencesTest {
    public static void main (String[] args) {
        Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();
        PropositionSymbol aa = new PropositionSymbol("A");
        PropositionSymbol bb = new PropositionSymbol("B");
        PropositionSymbol cc = new PropositionSymbol("C");
        ComplexSentence complex = new ComplexSentence(Connective.AND, aa, bb);
        Scanner sc = new Scanner(System.in);
        values.put(aa, true);
        values.put(bb, true);

        Model model = new Model(values);
        System.out.println("Model getValue: " + model.getValue(aa));
        System.out.println("Boolean getValue: " + model.isTrue(aa));
        System.out.println("Complex sentence is and sentence: " + complex.isAndSentence());

        int dimension = Integer.parseInt(sc.nextLine());
        SudokuLaboratory sl = new SudokuLaboratory(dimension);
        System.out.println(sl.symbols);
        System.out.println("Symbols count: " + sl.symbols.size());

        Sentence testDisj = complex.newDisjunction(sl.getSymbolsInRow(1));
        Model newModel = sl.assignValuesInOrder(1);
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
