/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.World;
import evopaint.interfaces.IAction;
import evopaint.interfaces.ICondition;

/**
 *
 * @author tam
 */
public class Rule {
    private ICondition condition;
    private IAction action;

    @Override
    public String toString() {
        return "if " + condition + " then " + action;
    }

    public boolean apply(Pixel pixel, World world) {
        if (condition.isMet(pixel, world) == false) {
            return false;
        }
        pixel.reward(action.execute(pixel, world));
        return true;
    }

    public Rule(ICondition condition, IAction action) {
        this.condition = condition;
        this.action = action;
    }
}
