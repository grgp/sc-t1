import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import java.util.Map;
import java.util.HashMap;

public class SentencesTest {
    public static void main (String[] args) {
        Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();
        PropositionSymbol aa = new PropositionSymbol("A");
        PropositionSymbol bb = new PropositionSymbol("B");
        PropositionSymbol cc = new PropositionSymbol("C");
        values.put(aa, true);
        values.put(bb, false);
        values.put(cc, false);

        Model model = new Model(values);
        System.out.println(model.getValue(bb));
    }
}
