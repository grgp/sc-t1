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
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.kb.data.Clause;

public class Tugas1B_factories {
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

    public Sentence cellSatifiesSudoku() {
        ComplexSentence all_sat = (ComplexSentence) cellUniqueInRows();
        return all_sat;
    }

    private Sentence cellUniqueInRows() {
        ComplexSentence all_rows = null;
        for (int index = 1; index <= this.dimension; index++) {
            ComplexSentence unique_row = (ComplexSentence) eachCellUniqueInRow(index);
            if (all_rows == null) {
                all_rows = unique_row;
            } else {
                all_rows = new ComplexSentence(all_rows, Connective.AND, unique_row);
            }
        }
        return all_rows;
    }

    private Sentence eachCellUniqueInRow(int row_index) {
        ComplexSentence row = null;
        for (int value = 1; value <= dimension; value++) {
            for (int cell = 1; cell < dimension; cell++) {
                ComplexSentence cs1 = new ComplexSentence(Connective.NOT,
                            symbols.get("x"+row_index+"y"+cell+"z"+value));
                ComplexSentence cs2 = new ComplexSentence(Connective.NOT,
                            symbols.get("x"+row_index+"y"+(cell+1)+"z"+value));
                ComplexSentence csDisj = new ComplexSentence(cs1, Connective.OR, cs2);

                if (row == null) {
                    row = csDisj;
                } else {
                    row = new ComplexSentence(row, Connective.AND, csDisj);
                }
            }
        }
        return row;
    }
}
