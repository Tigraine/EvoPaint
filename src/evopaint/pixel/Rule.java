/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.World;
import evopaint.interfaces.IAction;
import evopaint.interfaces.IRule;
import evopaint.pixel.actions.ActionWrapper;
import evopaint.pixel.conditions.ConditionWrapper;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tam
 */
public class Rule implements IRule {
    private List<ConditionWrapper> conditions;
    private List<ActionWrapper> actions;

    public List<ConditionWrapper> getConditions() {
        return conditions;
    }

    public List<ActionWrapper> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        String ret = "IF ";
        for (Iterator<ConditionWrapper> ii = conditions.iterator(); ii.hasNext();) {
            ConditionWrapper directedCondition = ii.next();
            ret += directedCondition;
            if (ii.hasNext()) {
                ret += " AND ";
            }
        }
        ret += " THEN ";
        for (Iterator<ActionWrapper> ii = actions.iterator(); ii.hasNext();) {
            ActionWrapper directedAction = ii.next();
            ret += directedAction;
            if (ii.hasNext()) {
                ret += " AND ";
            }
        }
        return ret;
    }

    public boolean apply(Pixel pixel, World world) {
        for (ConditionWrapper condition : conditions) {
            if (condition.isMet(pixel, world) == false) {
                return false;
            }
        }

        for (ActionWrapper wrappedAction : actions) {
            pixel.reward(wrappedAction.execute(pixel, world));
        }

        return true;
    }

    public Rule(List<ConditionWrapper> conditions, List<ActionWrapper> actions) {
        this.conditions = conditions;
        this.actions = actions;
    }
}
