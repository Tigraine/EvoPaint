/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public RewardAction(int cost, List<RelativeCoordinate> directions, int rewardValue) {
        super("reward", cost, directions);
        this.rewardValue = rewardValue;
    }

    public RewardAction() {
        super("reward");
    }

    public int getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(int rewardValue) {
        this.rewardValue = rewardValue;
    }

    public void executeCallback(Pixel origin, RelativeCoordinate direction, World world) {
        Pixel target = world.get(origin.getLocation(), direction);
        if (target == null) { // cannot reward nothingness
            return;
        }
        target.reward(rewardValue);
    }

    protected Map<String, String>parametersCallbackString(Map<String, String> map) {
        map.put("reward", Integer.toString(rewardValue));
        return map;
    }

    protected Map<String, String>parametersCallbackHTML(Map<String, String> map) {
        map.put("Reward", Integer.toString(rewardValue));
        return map;
    }

    public LinkedHashMap<String,JComponent> parametersCallbackGUI(LinkedHashMap<String, JComponent> parametersMap) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(rewardValue, 0, Integer.MAX_VALUE, 1);
        JSpinner rewardValueSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        rewardValueSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setRewardValue((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        parametersMap.put("Reward", rewardValueSpinner);

        return parametersMap;
    }
}
