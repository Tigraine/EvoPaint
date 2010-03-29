/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
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
        ret += "reward: " + rewardValue;
        ret += ", ";
        ret += super.toString();
        return ret;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI() {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(rewardValue, 0, Integer.MAX_VALUE, 1);
        JSpinner energyValueSpinner = new JSpinner(spinnerModel);
        energyValueSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setRewardValue((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        // getting too big because Integer.MAX_VALUE can get pretty long, so let's cut it off
        energyValueSpinner.setPreferredSize(new Dimension(100, energyValueSpinner.getPreferredSize().height));
        final JFormattedTextField spinnerText = ((JSpinner.DefaultEditor)energyValueSpinner.getEditor()).getTextField();
        spinnerText.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() { // only seems to work this way
                        public void run() {
                            spinnerText.selectAll();
                        }
                });
            }
            public void focusLost(FocusEvent e) {}
        });
        ret.put("Reward", energyValueSpinner);

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
