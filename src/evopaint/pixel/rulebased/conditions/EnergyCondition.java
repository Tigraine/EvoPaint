/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.pixel.rulebased.AbstractCondition;
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

    public EnergyCondition(int min, int max, List<RelativeCoordinate> directions, NumberComparisonOperator comparisonOperator, int energyValue) {
        super("energy", min, max, directions);
        this.comparisonOperator = comparisonOperator;
        this.energyValue = energyValue;
    }

    public EnergyCondition() {
        super("energy", 0, 0, new ArrayList<RelativeCoordinate>(9));
        this.comparisonOperator = NumberComparisonOperator.EQUAL;
        this.energyValue = 0;
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

    protected boolean isMetCallback(Pixel us, Pixel them) {
        if (them == null) { // never forget to skip empty spots
            return false;
        }
        return comparisonOperator.compare(them.getEnergy(), energyValue);
    }

    public String toStringCallback(String conditionString) {
        conditionString += "have ";
        conditionString += comparisonOperator.toString();
        conditionString += " ";
        conditionString += energyValue;
        conditionString += " energy";
        return conditionString;
    }

    public String toHTMLCallback(String conditionString) {
        conditionString += "have ";
        conditionString += comparisonOperator.toHTML();
        conditionString += " ";
        conditionString += energyValue;
        conditionString += " energy";
        return conditionString;
    }

    public LinkedHashMap<String,JComponent> parametersCallbackGUI(LinkedHashMap<String,JComponent> parametersMap) {
        JComboBox comparisonComboBox = new JComboBox(NumberComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(comparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        parametersMap.put("Comparison", comparisonComboBox);
       
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(energyValue, 0, Integer.MAX_VALUE, 1);
        JSpinner energyValueSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        energyValueSpinner.addChangeListener(new ValueListener());
        parametersMap.put("Value", energyValueSpinner);

        return parametersMap;
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
