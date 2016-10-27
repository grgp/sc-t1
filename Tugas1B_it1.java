import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import java.util.Scanner;

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

// class SudoBox extends PropositionSymbol {
//
// }
