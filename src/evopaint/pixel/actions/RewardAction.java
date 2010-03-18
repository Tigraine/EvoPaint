/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.actions;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.interfaces.IAction;

/**
 *
 * @author tam
 */
public class RewardAction extends AbstractAction implements IAction {

    protected int reward;

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return super.toString() + " " + reward;
    }

    public int execute(Pixel pixel, World world) {
        return reward;
    }

    public RewardAction(int reward) {
        super(0, "reward");
        this.reward = reward;
    }
}
