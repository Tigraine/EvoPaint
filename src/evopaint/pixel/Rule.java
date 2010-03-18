/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.World;
import evopaint.interfaces.IAction;
import evopaint.interfaces.IRequirement;

/**
 *
 * @author tam
 */
public class Rule {
    private IRequirement requirement;
    private IAction action;

    @Override
    public String toString() {
        return "if " + requirement + " then " + action;
    }

    public boolean apply(Pixel pixel, World world) {
        if (requirement.isMet(pixel, world) == false) {
            return false;
        }
        pixel.reward(action.execute(pixel, world));
        return true;
    }

    public Rule(IRequirement requirement, IAction action) {
        this.requirement = requirement;
        this.action = action;
    }
}
