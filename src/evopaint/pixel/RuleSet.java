/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.pixel.interfaces.IRule;
import java.util.List;

/**
 *
 * @author tam
 */
public class RuleSet {
    private String name;
    private List<IRule> rules;
    private List<State> availableStates;
    private State initialState;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String ret = "Rule set '" + name + "' consists of:\n";
        for (IRule rule : rules) {
            ret += "\t" + rule.toString() + "\n";
        }
        return ret;
    }

    public List<IRule> getRules() {
        return rules;
    }

    public void setRules(List<IRule> rules) {
        this.rules = rules;
    }

    public List<State> getAvailableStates() {
        return availableStates;
    }

    public void setAvailableStates(List<State> availableStates) {
        this.availableStates = availableStates;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public RuleSet(String name, List<IRule> rules, List<State> availableStates, State initialState) {
        this.name = name;
        this.rules = rules;
        this.availableStates = availableStates;
        this.initialState = initialState;
    }

    public RuleSet(RuleSet ruleSet) {
        this.name = ruleSet.name;
        this.rules = ruleSet.rules;
        this.availableStates = ruleSet.availableStates;
        this.initialState = ruleSet.initialState;
    }
}
