/**
 * @author: George Albert
 * @author_npm: 1406569781
 * @class: Sistem Cerdas A
 *
 * Class containing the main program to read the input, construct the
 * neccessary sentences and/or clauses, execute a SAT checking before finally
 * writing the output to a file.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.List;

import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.inference.OptimizedDPLL;
import aima.core.logic.propositional.inference.WalkSAT;

public class Tugas1B {
    public static void main(String[] args) {
        try {
            Debugger db = new Debugger();
            if (args.length > 3 && args[4].equals("debug")) {
                db.toggleDebugging();
            }

            // initialize I/O
            Scanner sc = null;
            PrintWriter pw = null;
            String output_filename = "";
            if (args.length >= 3) {
                File input_file = new File(args[1]);
                sc = new Scanner(input_file);
                output_filename = args[2];

                File output_file = new File(output_filename);
                FileWriter fw = new FileWriter(output_file);
                pw = new PrintWriter(fw);

            } else {
                System.out.println("Error: Missing arguments for program");
                System.exit(0);
            }

            int dimension = Integer.parseInt(sc.nextLine());
            SudokuFactory sf = new SudokuFactory(dimension);

            String ns = "";
            for (int row = 1; row <= dimension; row++) {
                for (int col = 1; col <= dimension; col++) {
                    int val = sc.nextInt();
                    String key = "x"+row+"y"+col+"z"+val;
                    if (val >= 1 && val <= dimension) {
                        ns += key + " ";
                        sf.kb.tell(sf.symbols.get(key));
                    }
                }
                ns += "\n";
            }

            db.log("number of clauses: " + sf.kb.asCNF().size());

            if (args[0].equals("walksat")) {
                WalkSAT ws = new WalkSAT();
                Model model = ws.walkSAT(sf.kb.asCNF(), 0.5, -1);
                printSudokuModel(sf, model, pw);
            } else if (args[0].equals("dpll")) {
                ModifiedRegularDPLL dpll = new ModifiedRegularDPLL();
                boolean b = dpll.dpllModified(sf.kb);
                Model model = dpll.model;
                printSudokuModel(sf, model, pw);
            } else if (args[0].equals("dpllmod")) {
                ModifiedOptimizedDPLL dpll = new ModifiedOptimizedDPLL();
                boolean b = dpll.dpllModified(sf.kb);
                Model model = dpll.model;
                printSudokuModel(sf, model, pw);
            }

            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * A helper method to print the retrieved Sudoku model.
     * @param  SudokuFactory object to retrieve the dimension and symbols
     * @param  Model object represents a correct solution
     * @param  PrintWriter object is passed from main, to write to an output file
     */
    private static void printSudokuModel(SudokuFactory sf, Model model,
                                         PrintWriter pw) {
        try {
            outerloop:
            for (int row = 1; row <= sf.dimension; row++) {
                for (int col = 1; col <= sf.dimension; col++) {
                    for (int val = 1; val <= sf.dimension; val++) {
                        String key = "x"+row+"y"+col+"z"+val;
                        if (model == null) {
                            System.out.println("Error: Either no solution is available, or input is invalid");
                            break outerloop;
                        }
                        if (model.getValue(sf.symbols.get(key)) == null) {
                            pw.print("_ ");
                        } else if (model.getValue(sf.symbols.get(key))) {
                            pw.print(val + " ");
                        }
                    }
                }
                pw.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
