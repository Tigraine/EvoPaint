/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.actions;

import evopaint.World;
import evopaint.interfaces.IAction;
import evopaint.pixel.Pixel;
import evopaint.util.logging.Logger;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class ActionWrapper {
    private RelativeCoordinate direction;
    private IAction action;

    @Override
    public String toString() {
        return action.toString().concat(" ").concat(direction.toString());
    }

    public RelativeCoordinate getDirection() {
        return direction;
    }
    
    public IAction getAction() {
        return action;
    }

    public int execute(Pixel us, World world) {
        if (us.getEnergy() - (-1) * action.getReward() < 0) {
            Logger.log.information("%s wanted to execute '%s' but does not have enough energy", us, action);
            return 0;
        }
        return action.execute(us, direction, world);
    }

    public ActionWrapper(RelativeCoordinate direction, IAction action) {
        this.direction = direction;
        this.action = action;
    }
}
