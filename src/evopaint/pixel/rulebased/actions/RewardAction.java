/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author tam
 */
public class RewardAction extends AbstractAction {
    
    private int rewardValue;

    public String getName() {
        return "Reward";
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
        ret += "targets: " + getDirectionsString();
        ret += ", reward: " + rewardValue;
        ret += ", cost: " + getCost();
        ret += ")";
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = "<b>reward</b>(";
        ret += "<span style='color: #777777;'>targets:</span> " + getDirectionsString();
        ret += ", <span style='color: #777777;'>reward:</span> " + rewardValue;
        ret += ", <span style='color: #777777;'>cost:</span> " + getCost();
        ret += ")";
        return ret;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI() {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(rewardValue, 0, Integer.MAX_VALUE, 1);
        JSpinner rewardValueSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        rewardValueSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setRewardValue((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        ret.put("Reward", rewardValueSpinner);

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
