import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.AtomicSentence;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import java.util.Map;
import java.util.HashMap;

public class SentencesTest {
    public static void main (String[] args) {
        Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();
        PropositionSymbol aa = new PropositionSymbol("A");
        PropositionSymbol bb = new PropositionSymbol("B");
        PropositionSymbol cc = new PropositionSymbol("C");
        ComplexSentence complex = new ComplexSentence(Connective.AND, aa, bb);
        values.put(aa, true);
        values.put(bb, true);

        Model model = new Model(values);
        System.out.println("Model getValue: " + model.getValue(aa));
        System.out.println("Boolean getValue: " + model.isTrue(aa));
        System.out.println("Complex sentence is and sentence: " + complex.isAndSentence());
    }
}
