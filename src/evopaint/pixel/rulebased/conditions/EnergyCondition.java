/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.gui.rulesetmanager.JTargetButton;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.pixel.rulebased.Condition;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.Pixel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
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
public class EnergyCondition extends Condition {

    private NumberComparisonOperator comparisonOperator;
    private int energyValue;

    public EnergyCondition(IConditionTarget target, NumberComparisonOperator comparisonOperator, int energyValue) {
        super(target);
        this.comparisonOperator = comparisonOperator;
        this.energyValue = energyValue;
    }

    public EnergyCondition() {
        comparisonOperator = NumberComparisonOperator.GREATER_THAN;
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

    public String getName() {
        return "energy";
    }

    public boolean isMet(Pixel us, Pixel them) {
        if (them == null) { // never forget to skip empty spots
            return false;
        }
        return comparisonOperator.compare(them.getEnergy(), energyValue);
    }

    @Override
    public String toString() {
        String conditionString = new String();
        conditionString += "energy of ";
        conditionString += getTarget().toString();
        conditionString += " is ";
        conditionString += comparisonOperator.toString();
        conditionString += " ";
        conditionString += energyValue;
        return conditionString;
    }

    public String toHTML() {
        String conditionString = new String();
        conditionString += "energy of ";
        conditionString += getTarget().toHTML();
        conditionString += " is ";
        conditionString += comparisonOperator.toHTML();
        conditionString += " ";
        conditionString += energyValue;
        return conditionString;
    }

    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String,JComponent> parametersMap) {
        JTargetButton jTargetButton = new JTargetButton(this);
        parametersMap.put("Target", jTargetButton);

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
