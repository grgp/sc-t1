import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.kb.data.Clause;

public class Tugas1C_factories {
}

class SudokuFactory {
    final int dimension;
    final HashMap<String, PropositionSymbol> symbols;
    KnowledgeBase kb;

    public SudokuFactory(int dimension) {
        this.dimension = dimension;
        this.symbols = generateSymbols();
        this.kb = new KnowledgeBase();
        cellSatifiesSudoku();
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

    private void cellSatifiesSudoku() {
        cellAtLeastOne();
        cellUniqueInRowsCols();
    }

    private void cellAtLeastOne() {
        for (int row = 1; row <= dimension; row++) {
            for (int col = 1; col <= dimension; col++) {
                ArrayList<Sentence> for_each_cell = new ArrayList<Sentence>();
                for (int value = 1; value <= dimension; value++) {
                    for_each_cell.add(symbols.get("x"+row+"y"+col+"z"+value));
                }
                Sentence sc = Sentence.newDisjunction(for_each_cell);
                kb.tell(sc);
            }
        }
    }

    private void cellUniqueInRowsCols() {
        for (int index = 1; index <= this.dimension; index++) {
            ComplexSentence unique_row = eachCellUniqueInRow(index);
            ComplexSentence unique_col = eachCellUniqueInCol(index);
            kb.tell(unique_row);
            kb.tell(unique_col);
        }
    }

    private ComplexSentence eachCellUniqueInRow(int index) {
        ComplexSentence row = null;
        for (int value = 1; value <= dimension; value++) {
            for (int cell = 1; cell < dimension; cell++) {
                ComplexSentence cs1 = new ComplexSentence(Connective.NOT,
                            symbols.get("x"+index+"y"+cell+"z"+value));

                for (int comparedCell = cell+1; comparedCell <= dimension; comparedCell++) {
                    ComplexSentence cs2 = new ComplexSentence(Connective.NOT,
                                symbols.get("x"+index+"y"+comparedCell+"z"+value));
                    ComplexSentence csDisj = new ComplexSentence(cs1, Connective.OR, cs2);

                    if (row == null) {
                        row = csDisj;
                    } else {
                        row = new ComplexSentence(row, Connective.AND, csDisj);
                    }
                }
            }
        }
        return row;
    }

    private ComplexSentence eachCellUniqueInCol(int index) {
        ComplexSentence col = null;
        for (int value = 1; value <= dimension; value++) {
            for (int cell = 1; cell < dimension; cell++) {
                ComplexSentence cs1 = new ComplexSentence(Connective.NOT,
                            symbols.get("x"+cell+"y"+index+"z"+value));
                ComplexSentence cs2 = new ComplexSentence(Connective.NOT,
                            symbols.get("x"+(cell+1)+"y"+index+"z"+value));
                ComplexSentence csDisj = new ComplexSentence(cs1, Connective.OR, cs2);

                if (col == null) {
                    col = csDisj;
                } else {
                    col = new ComplexSentence(col, Connective.AND, csDisj);
                }
            }
        }
        return col;
    }

}
