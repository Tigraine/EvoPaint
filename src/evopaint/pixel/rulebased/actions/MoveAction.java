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
public class MoveAction extends Action {

    public MoveAction(int energyChange, IActionTarget target) {
        super(energyChange, target);
    }

    public MoveAction() {
    }

    public String getName() {
        return "move";
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        Pixel target = configuration.world.get(actor.getLocation(), direction);
        if (target != null) {
            return 0;
        }
        configuration.world.remove(actor.getLocation());
        actor.getLocation().move(direction, configuration.world);
        configuration.world.set(actor);

        return energyChange;
    }
    
}
