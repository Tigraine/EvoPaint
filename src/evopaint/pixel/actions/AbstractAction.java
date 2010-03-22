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
public abstract class AbstractAction implements IAction {
    private String name;
    private int reward;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.concat("(").concat(Integer.toString(reward)).concat(")");
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public abstract int execute(Pixel pixel, RelativeCoordinate direction, World world);

    public AbstractAction(String name, int reward) {
        this.name = name;
        this.reward = reward;
    }
}
