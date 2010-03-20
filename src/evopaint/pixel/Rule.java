/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.World;
import evopaint.interfaces.IAction;
import evopaint.interfaces.ICondition;
import evopaint.interfaces.IRule;
import java.util.List;

/**
 *
 * @author tam
 */
public class Rule implements IRule {
    private List<ICondition> conditions;
    private IAction action;

    public IAction getAction() {
        return action;
    }

    public List<ICondition> getConditions() {
        return conditions;
    }

    @Override
    public String toString() {
        String ret = "IF ";
        for (ICondition condition : conditions) {
            ret += condition + " AND";
        }
        ret.substring(0, ret.length() - 3);
        ret += " THEN " + action;
        return ret;
    }

    public boolean apply(Pixel pixel, World world) {
        for (ICondition condition : conditions) {
            if (condition.isMet(pixel, world) == false) {
                return false;
            }
        }
        
        pixel.reward(action.execute(pixel, world));
        return true;
    }

    public Rule(List<ICondition> conditions, IAction action) {
        this.conditions = conditions;
        this.action = action;
    }
}
