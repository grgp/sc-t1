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

public class DPLLTest {
    public static void main(String[] args) {
        Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();
        Literal aa = new Literal(new PropositionSymbol("A"));
        Literal bb = new Literal(new PropositionSymbol("B"));
        Literal cc = new Literal(new PropositionSymbol("C"));
        Literal dd = new Literal(new PropositionSymbol("D"));
        Clause clau1 = new Clause(aa, bb, cc);
        DPLLSatisfiable dpls = new DPLLSatisfiable();

        Scanner sc = new Scanner(System.in);
        values.put(aa, true);
        values.put(bb, false);
        values.put(cc, false);

        Model model = new Model(values);
        System.out.println("Model dpll satisfiable : " + model.isTrue(complex));
    }
}
