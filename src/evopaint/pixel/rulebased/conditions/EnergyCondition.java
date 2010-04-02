/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.World;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author tam
 */
public class EnergyCondition extends AbstractCondition {

    private NumberComparisonOperator comparisonOperator;
    private int energyValue;

    public String getName() {
        return "Energy";
    }

    public NumberComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(NumberComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(int energyValue) {
        this.energyValue = energyValue;
    }

    @Override
    public String toString() {
         String ret = new String();
        ret += getDirectionsString();
        ret += super.getDirections().size() > 1 ? " have " : " has ";
        ret += comparisonOperator.toString();
        ret += " ";
        ret += energyValue;
        ret += " energy";
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = new String();
        ret += getDirectionsString();
        ret += super.getDirections().size() > 1 ? " have " : " has ";
        ret += comparisonOperator.toHTML();
        ret += " ";
        ret += energyValue;
        ret += " energy";
        return ret;
    }

    @Override
    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            if (comparisonOperator.compare(them.getEnergy(), energyValue) == false) {
                return false; // so this is what lazy evaluation looks like...
            }
        }
        return true;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration) {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();

        JComboBox comparisonComboBox = new JComboBox(NumberComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(comparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        ret.put("Comparison", comparisonComboBox);
       
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(energyValue, 0, Integer.MAX_VALUE, 1);
        JSpinner energyValueSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        energyValueSpinner.addChangeListener(new ValueListener());
        ret.put("Value", energyValueSpinner);

        return ret;
    }

    public EnergyCondition(List<RelativeCoordinate> directions, NumberComparisonOperator comparisonOperator, int energyValue) {
        super(directions);
        this.comparisonOperator = comparisonOperator;
        this.energyValue = energyValue;
    }

    public EnergyCondition() {
        super(new ArrayList<RelativeCoordinate>(9));
        this.comparisonOperator = NumberComparisonOperator.EQUAL;
        this.energyValue = 0;
    }

    private class ComparisonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setComparisonOperator((NumberComparisonOperator) ((JComboBox) (e.getSource())).getSelectedItem());
        }
    }

    private class ValueListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            setEnergyValue((Integer)((JSpinner)e.getSource()).getValue());
        }

    }
}
