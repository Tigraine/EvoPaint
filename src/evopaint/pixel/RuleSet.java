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

    public RuleSet(String name, List<IRule> rules) {
        this.name = name;
        this.rules = rules;
    }
}
