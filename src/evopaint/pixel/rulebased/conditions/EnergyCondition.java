/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.gui.rulesetmanager.JConditionTargetButton;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.pixel.rulebased.Condition;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.RuleBasedPixel;
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
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
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

    public int getType() {
        return Condition.ENERGY;
    }

    @Override
    public void mixWith(Condition theirCondition, float theirShare, IRandomNumberGenerator rng) {
        super.mixWith(theirCondition, theirShare, rng);
        EnergyCondition c = (EnergyCondition)theirCondition;
        if (rng.nextFloat() < theirShare) {
            energyValue = c.energyValue;
        }
        if (rng.nextFloat() < theirShare) {
            comparisonOperator = c.comparisonOperator;
        }
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

    public boolean isMet(RuleBasedPixel actor, RuleBasedPixel target) {
        if (target == null) { // never forget to skip empty spots
            return false;
        }
        return comparisonOperator.compare(target.getEnergy(), energyValue);
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
        JConditionTargetButton JConditionTargetButton = new JConditionTargetButton(this);
        parametersMap.put("Target", JConditionTargetButton);

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
