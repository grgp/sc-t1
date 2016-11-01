/**
 * @author: George Albert
 * @author_npm: 1406569781
 * @class: Sistem Cerdas A
 *
 * SudokuFactory class is the primary class that would generateSymbols
 * all the Sentences, Symbols, etc., based on the input given. It's also
 * connected to the KnowledgeBase, which can be updated (unlike the other
 * variables that are declared final).
 */

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

class SudokuFactory {
    final int dimension;
    final int box_dimension;
    final HashMap<String, PropositionSymbol> symbols;
    KnowledgeBase kb;

    public SudokuFactory(int dimension) {
        this.dimension = dimension;
        this.symbols = generateSymbols();
        this.kb = new KnowledgeBase();
        this.box_dimension = (int) Math.sqrt(dimension);
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
        if (dimension == 9) {
            cellUniqueInBoxes();
        }
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

    private void cellUniqueInBoxes() {
        for (int value = 1; value <= dimension; value++) {
            for (int i = 0; i < box_dimension; i++) {
                for (int j = 0; j < box_dimension; j++) {
                    eachCellUniqueInBox(value, i, j);
                }
            }
        }
    }

    private void eachCellUniqueInBox(int value, int i, int j) {
        ComplexSentence box = null;
        for (int x = 1; x <= box_dimension; x++) {
            for (int y = 1; y <= box_dimension; y++) {
                ComplexSentence cs1 = new ComplexSentence(Connective.NOT,
                            symbols.get("x"+(3*i+x)+"y"+(3*j+y)+"z"+value));
                for (int k = y+1; k <= box_dimension; k++) {
                    ComplexSentence cs2 = new ComplexSentence(Connective.NOT,
                                symbols.get("x"+(3*i+x)+"y"+(3*j+k)+"z"+value));
                    ComplexSentence csDisj = new ComplexSentence(cs1, Connective.OR, cs2);
                    kb.tell(csDisj);
                }
                for (int k = x+1; k <= box_dimension; k++) {
                    for (int l = 1; l <= box_dimension; l++) {
                        ComplexSentence cs2 = new ComplexSentence(Connective.NOT,
                                    symbols.get("x"+(3*i+k)+"y"+(3*j+l)+"z"+value));
                        ComplexSentence csDisj = new ComplexSentence(cs1, Connective.OR, cs2);
                        kb.tell(csDisj);
                    }
                }
            }
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

                for (int comparedCell = cell+1; comparedCell <= dimension; comparedCell++) {
                    ComplexSentence cs2 = new ComplexSentence(Connective.NOT,
                                symbols.get("x"+comparedCell+"y"+index+"z"+value));
                    ComplexSentence csDisj = new ComplexSentence(cs1, Connective.OR, cs2);

                    if (col == null) {
                        col = csDisj;
                    } else {
                        col = new ComplexSentence(col, Connective.AND, csDisj);
                    }
                }
            }
        }
        return col;
    }

}

class Debugger {
    private boolean enabled;

    public Debugger() {
        this.enabled = false;
    }

    public void toggleDebugging() {
        this.enabled = !this.enabled;
    }

    public void log(String s) {
        if (enabled) {
            System.out.println(s);
        }
    }
}
