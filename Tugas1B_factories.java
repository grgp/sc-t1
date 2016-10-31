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
    public static void main(String[] args) {

    }
}

class SudokuSentenceBuilder {
    final int dimension;
    final HashMap<String, PropositionSymbol> symbols;

    public SudokuSentenceBuilder(int dimension, HashMap<String, PropositionSymbol> symbols) {
        this.dimension = dimension;
        this.symbols = symbols;
    }

    public Sentence cellSatifiesSudoku() {
        ComplexSentence all_sat = new ComplexSentence(Connective.AND, cellUniqueInRows());
        return all_sat;
    }

    private Sentence cellUniqueInRows() {
        ComplexSentence all_rows = new ComplexSentence(Connective.AND);
        for (int index = 1; index <= this.dimension; index++) {
            Sentence unique_row = eachCellUniqueInRow(index);
            all_rows = new ComplexSentence(all_rows, Connective.AND, unique_row);
        }
        return all_rows;
    }

    private Sentence eachCellUniqueInRow(int row_index) {
        ComplexSentence row = new ComplexSentence(Connective.AND);
        for (int value = 1; value <= dimension; value++) {
            for (int cell = 1; cell < dimension; cell++) {
                Literal lit1 = new Literal(symbols.get("x"+row_index+"y"+cell+"z"+value), false);
                Literal lit2 = new Literal(symbols.get("x"+row_index+"y"+(cell+1)+"z"+value), false);

                Clause clause = new Clause(lit1, lit2);
                row = new ComplexSentence(row, Connective.AND);
            }
        }
    }
}
