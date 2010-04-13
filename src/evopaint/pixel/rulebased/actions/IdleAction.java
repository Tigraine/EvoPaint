/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class IdleAction extends Action {

    public IdleAction(int energyChange) {
        super(energyChange, null);
    }

    public IdleAction() {
    }

    public String getName() {
        return "idle";
    }

    @Override // doing nothing does not need target checking
    public int execute(Pixel actor, Configuration configuration) {
        return energyChange;
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        // MEEP MEEEEEEP, I CAN HAS NEVAR BE CALLED!
        assert (false);
        return energyChange;
    }

}
