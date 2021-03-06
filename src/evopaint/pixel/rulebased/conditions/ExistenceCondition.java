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
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.util.ObjectComparisonOperator;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.targeting.MetaTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ExistenceCondition extends Condition {

    private ObjectComparisonOperator objectComparisonOperator;

    public ExistenceCondition(IConditionTarget target, ObjectComparisonOperator objectComparisonOperator) {
        super(target);
        this.objectComparisonOperator = objectComparisonOperator;
    }

    public ExistenceCondition() {
        objectComparisonOperator = ObjectComparisonOperator.EQUAL;
    }

    public ExistenceCondition(ExistenceCondition existenceCondition) {
        super(existenceCondition);
        this.objectComparisonOperator = existenceCondition.objectComparisonOperator;
    }

    public ExistenceCondition(IRandomNumberGenerator rng) {
        super(rng);
        this.objectComparisonOperator = ObjectComparisonOperator.getRandom(rng);
    }

    public int getType() {
        return Condition.EXISTENCE;
    }

    @Override
    public int countGenes() {
        return super.countGenes() + 1;
    }

    @Override
    public void mutate(int mutatedGene, IRandomNumberGenerator rng) {
        int numGenesSuper = super.countGenes();
        if (mutatedGene < numGenesSuper) {
            super.mutate(mutatedGene, rng);
            return;
        }
        mutatedGene -= numGenesSuper;

        if (mutatedGene == 0) {
            objectComparisonOperator = ObjectComparisonOperator.getRandomOtherThan(objectComparisonOperator, rng);
            return;
        }

        assert false; // we have an error in the mutatedGene calculation
    }

    @Override
    public void mixWith(Condition theirCondition, float theirShare, IRandomNumberGenerator rng) {
        super.mixWith(theirCondition, theirShare, rng);
        ExistenceCondition c = (ExistenceCondition)theirCondition;
        if (rng.nextFloat() < theirShare) {
            objectComparisonOperator = c.objectComparisonOperator;
        }
    }

    public ObjectComparisonOperator getComparisonOperator() {
        return objectComparisonOperator;
    }

    public void setComparisonOperator(ObjectComparisonOperator comparisonOperator) {
        this.objectComparisonOperator = comparisonOperator;
    }

    public String getName() {
        return "existence";
    }

    public boolean isMet(RuleBasedPixel actor, RuleBasedPixel target) {
        return !objectComparisonOperator.compare(target, null);
    }

    @Override
    public String toString() {
        String conditionString = new String();
        conditionString += getTarget().toString();
        if (getTarget() instanceof MetaTarget && ((MetaTarget)getTarget()).getDirections().size() > 1) {
            if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
                conditionString += " are free spots";
            } else {
                conditionString += " are pixels";
            }
        } else {
            if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
                conditionString += " is a free spot";
            } else {
                conditionString += " is a pixel";
            }
        }
        return conditionString;
    }

    public String toHTML() {
        String conditionString = new String();
        conditionString += getTarget().toHTML();
        if (getTarget() instanceof MetaTarget && ((MetaTarget)getTarget()).getDirections().size() > 1) {
            if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
                conditionString += " are free spots";
            } else {
                conditionString += " are pixels";
            }
        } else {
            if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
                conditionString += " is a free spot";
            } else {
                conditionString += " is a pixel";
            }
        }
        return conditionString;
    }

    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String,JComponent> parametersMap) {
        JConditionTargetButton JConditionTargetButton = new JConditionTargetButton(this);
        parametersMap.put("Target", JConditionTargetButton);

        JComboBox comparisonComboBox = new JComboBox(ObjectComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(objectComparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        parametersMap.put("Comparison", comparisonComboBox);

        return parametersMap;
    }

    private class ComparisonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setComparisonOperator((ObjectComparisonOperator) ((JComboBox) (e.getSource())).getSelectedItem());
        }
    }
}
