/**
 * @author: George Albert
 * @author_npm: 1406569781
 * @class: Sistem Cerdas A
 */

import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.util.Util;
import aima.core.util.datastructure.Pair;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Model;

import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.inference.OptimizedDPLL;

class ModifiedRegularDPLL extends DPLLSatisfiable {
    Model model;

    public boolean dpllModified(KnowledgeBase kb) {
        Set<Clause> clauses = kb.asCNF();
        List<PropositionSymbol> symbols = new ArrayList<PropositionSymbol>(kb.getSymbols());
        return dpll(clauses, symbols, new Model());
    }

    @Override
    public boolean dpll(Set<Clause> clauses, List<PropositionSymbol> symbols,
            Model model) {
        // if every clause in clauses is true in model then return true
        if (everyClauseTrue(clauses, model)) {
            this.model = model;
            return true;
        }
        // if some clause in clauses is false in model then return false
        if (someClauseFalse(clauses, model)) {
            return false;
        }

        // P, value <- FIND-PURE-SYMBOL(symbols, clauses, model)
        Pair<PropositionSymbol, Boolean> pAndValue = findPureSymbol(symbols,
                clauses, model);
        // if P is non-null then
        if (pAndValue != null) {
            // return DPLL(clauses, symbols - P, model U {P = value})
            return dpll(clauses, minus(symbols, pAndValue.getFirst()),
                    model.union(pAndValue.getFirst(), pAndValue.getSecond()));
        }

        // P, value <- FIND-UNIT-CLAUSE(clauses, model)
        pAndValue = findUnitClause(clauses, model);
        // if P is non-null then
        if (pAndValue != null) {
            // return DPLL(clauses, symbols - P, model U {P = value})
            return dpll(clauses, minus(symbols, pAndValue.getFirst()),
                    model.union(pAndValue.getFirst(), pAndValue.getSecond()));
        }

        // P <- FIRST(symbols); rest <- REST(symbols)
        PropositionSymbol p = Util.first(symbols);
        List<PropositionSymbol> rest = Util.rest(symbols);
        // return DPLL(clauses, rest, model U {P = true}) or
        // ...... DPLL(clauses, rest, model U {P = false})
        return dpll(clauses, rest, model.union(p, true))
                || dpll(clauses, rest, model.union(p, false));
    }
}

class ModifiedOptimizedDPLL extends OptimizedDPLL {
    Model model;

    public boolean dpllModified(KnowledgeBase kb) {
        Set<Clause> clauses = kb.asCNF();
        List<PropositionSymbol> symbols = new ArrayList<PropositionSymbol>(kb.getSymbols());
        return dpll(clauses, symbols, new Model());
    }

    @Override
    public boolean dpll(Set<Clause> clauses, List<PropositionSymbol> symbols,
            Model model) {
        // if every clause in clauses is true in model then return true
        // if some clause in clauses is false in model then return false
        // NOTE: for optimization reasons we only want to determine the
        // values of clauses once on each call to dpll
        boolean allTrue = true;
        Set<Clause> unknownClauses = new LinkedHashSet<Clause>();
        for (Clause c : clauses) {
            Boolean value = model.determineValue(c);
            if (!Boolean.TRUE.equals(value)) {
                allTrue = false;
                if (Boolean.FALSE.equals(value)) {
                    return false;
                }
                unknownClauses.add(c);
            }
        }
        if (allTrue) {
            this.model = model;
            return true;
        }

        // NOTE: Performance Optimization -
        // Going forward, algorithm can ignore clauses that are already
        // known to be true (reduces overhead on recursive calls and simplifies
        // findPureSymbols() and findUnitClauses() logic as they can
        // always assume unknown).
        clauses = unknownClauses;

        // P, value <- FIND-PURE-SYMBOL(symbols, clauses, model)
        Pair<PropositionSymbol, Boolean> pAndValue = findPureSymbol(symbols,
                clauses, model);
        // if P is non-null then
        if (pAndValue != null) {
            // return DPLL(clauses, symbols - P, model U {P = value})
            return dpll(clauses, minus(symbols, pAndValue.getFirst()),
                    model.unionInPlace(pAndValue.getFirst(), pAndValue.getSecond()));
        }

        // P, value <- FIND-UNIT-CLAUSE(clauses, model)
        pAndValue = findUnitClause(clauses, model);
        // if P is non-null then
        if (pAndValue != null) {
            // return DPLL(clauses, symbols - P, model U {P = value})
            return dpll(clauses, minus(symbols, pAndValue.getFirst()),
                    model.unionInPlace(pAndValue.getFirst(), pAndValue.getSecond()));
        }

        // P <- FIRST(symbols); rest <- REST(symbols)
        PropositionSymbol p = Util.first(symbols);
        List<PropositionSymbol> rest = Util.rest(symbols);
        // return DPLL(clauses, rest, model U {P = true}) or
        // ...... DPLL(clauses, rest, model U {P = false})
        return callDPLL(clauses, rest, model, p, true)
                || callDPLL(clauses, rest, model, p, false);
    }

    protected boolean callDPLL(Set<Clause> clauses, List<PropositionSymbol> symbols,
            Model model, PropositionSymbol p, boolean value) {
        // We update the model in place with the assignment p=value,
        boolean result = dpll(clauses, symbols, model.unionInPlace(p, value));
        // as backtracking can occur during the recursive calls we
        // need to remove the assigned value before we pop back out from this
        // call.
        model.remove(p);
        if (result) {
            this.model = model;
        }
        return result;
    }
}
