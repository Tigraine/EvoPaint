/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class IdleAction extends Action {

    public IdleAction(int energyChange, IActionTarget target) {
        super(energyChange, target);
    }

    public IdleAction() {
    }

    public String getName() {
        return "idle";
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        // MEEP MEEEEEEP!
        return energyChange;
    }

}
