/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.Action;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
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
public class RewardAction extends Action {
    
    private int rewardValue;

    public RewardAction(int cost, IActionTarget target) {
        super(cost, target);
    }

    public RewardAction() {
    }

    public String getName() {
        return "reward";
    }

    public int getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(int rewardValue) {
        this.rewardValue = rewardValue;
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        Pixel target = configuration.world.get(actor.getLocation(), direction);
        if (target == null) {
            return 0;
        }
        target.reward(rewardValue);

        return cost;
    }

    @Override
    public Map<String, String>addParametersString(Map<String, String> parametersMap) {
        parametersMap = super.addParametersString(parametersMap);
        parametersMap.put("reward", Integer.toString(rewardValue));
        return parametersMap;
    }

    @Override
    public Map<String, String>addParametersHTML(Map<String, String> parametersMap) {
        parametersMap = super.addParametersHTML(parametersMap);
        parametersMap.put("Reward", Integer.toString(rewardValue));
        return parametersMap;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

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
