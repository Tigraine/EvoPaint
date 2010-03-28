/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tam
 */
public class RewardAction extends AbstractAction {
    
    private int rewardValue;

    public String getName() {
        return "reward";
    }

    public int getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(int rewardValue) {
        this.rewardValue = rewardValue;
    }

    @Override
    public String toString() {
        String ret = "reward(";
        ret += "reward: " + rewardValue;
        ret += ", ";
        ret += super.toString();
        return ret;
    }

    public int execute(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            them.reward(rewardValue);
        }
        return getCost();
    }

    public RewardAction(int cost, List<RelativeCoordinate> directions, int rewardValue) {
        super(cost, directions);
        this.rewardValue = rewardValue;
    }

    public RewardAction() {
        super(0, new ArrayList<RelativeCoordinate>(9));
    }
}
