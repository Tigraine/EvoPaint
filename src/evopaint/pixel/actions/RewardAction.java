/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.actions;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.interfaces.IAction;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class RewardAction extends AbstractAction implements IAction {

    @Override
    public String toString() {
        return super.toString();
    }

    public int execute(Pixel pixel, RelativeCoordinate direction, World world) {
        return getReward();
    }

    public RewardAction(int reward) {
        super("reward", reward);
    }
}
