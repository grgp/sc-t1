import java.util.Scanner;
import java.util.ArrayList;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

public class Tugas1B_it1 {
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            int dimension = Integer.parseInt(input.nextLine());

            String[][] rows;
            for (int i=0; i<dimension; i++) {
                //rows[i] = input.nextLine().split(" ");
            }

            String[][] priMap = new String[dimension][dimension];

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}

class SudokuFactory {
    final int dimension;
    final ArrayList<PropositionSymbol> symbols;

    public SudokuFactory(int dimension) {
        this.dimension = dimension;
        this.symbols = generateSymbols();
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

}
