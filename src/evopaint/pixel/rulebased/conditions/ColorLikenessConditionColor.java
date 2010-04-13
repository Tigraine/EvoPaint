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
import evopaint.gui.rulesetmanager.util.ColorChooserLabel;
import evopaint.gui.rulesetmanager.util.DimensionsListener;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ColorLikenessConditionColor extends Condition {

    private PixelColor comparedColor;
    private ColorDimensions dimensions;
    private int compareToLikenessPercentage;
    private NumberComparisonOperator comparisonOperator;

    public ColorLikenessConditionColor(IConditionTarget target, PixelColor comparedColor, ColorDimensions dimensions, int compareToLikenessPercentage, NumberComparisonOperator comparisonOperator) {
        super(target);
        this.comparedColor = comparedColor;
        this.dimensions = dimensions;
        this.compareToLikenessPercentage = compareToLikenessPercentage;
        this.comparisonOperator = comparisonOperator;
    }

    public ColorLikenessConditionColor() {
        comparedColor = new PixelColor(0);
        dimensions = new ColorDimensions(true, true, true);
        comparisonOperator = NumberComparisonOperator.GREATER_OR_EQUAL;
    }

    public int getCompareToLikenessPercentage() {
        return compareToLikenessPercentage;
    }

    public void setCompareToLikenessPercentage(int compareToLikenessPercentage) {
        this.compareToLikenessPercentage = compareToLikenessPercentage;
    }

    public PixelColor getComparedColor() {
        return comparedColor;
    }

    public void setComparedColor(PixelColor comparedColor) {
        this.comparedColor = comparedColor;
    }

    public NumberComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(NumberComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public ColorDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(ColorDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public String getName() {
        return "color likeness (compared to color)";
    }

    public boolean isMet(Pixel actor, Pixel target) {
        if (target == null) { // never forget to skip empty spots
            return false;
        }
        double distance = target.getPixelColor().distanceTo(comparedColor, dimensions);
        //System.out.println("distance: " + distance);
        int likenessPercentage = (int)((1 - distance) * 100);
        return comparisonOperator.compare(likenessPercentage, compareToLikenessPercentage);
    }

    @Override
    public String toString() {
        String conditionString = new String();
        conditionString += "color of ";
        conditionString += getTarget().toString();
        conditionString += " ";
        conditionString += comparisonOperator.toString();
        conditionString += " ";
        conditionString += compareToLikenessPercentage;
        conditionString += "% like ";
        conditionString += comparedColor.toString();
        conditionString += " (dimensions: ";
        conditionString += dimensions.toString();
        conditionString += ")";
        return conditionString;
    }

    public String toHTML() {
        String conditionString = new String();
        conditionString += "color of ";
        conditionString += getTarget().toHTML();
        conditionString += " ";
        conditionString += comparisonOperator.toHTML();
        conditionString += " ";
        conditionString += compareToLikenessPercentage;
        conditionString += "% like ";
        conditionString += comparedColor.toHTML();
        conditionString += " <span style='color: #777777;'>(dimensions: ";
        conditionString += dimensions.toHTML();
        conditionString += ")</span>";
        return conditionString;
    }

    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String,JComponent> parametersMap) {
        JConditionTargetButton JConditionTargetButton = new JConditionTargetButton(this);
        parametersMap.put("Target", JConditionTargetButton);

        ColorChooserLabel colorLabel = new ColorChooserLabel(comparedColor);
        JPanel wrapLabelToAvoidUncoloredStretchedBackground = new JPanel();
        wrapLabelToAvoidUncoloredStretchedBackground.add(colorLabel);
        parametersMap.put("Color", wrapLabelToAvoidUncoloredStretchedBackground);

        JPanel dimensionsPanel = new JPanel();
        JToggleButton btnH = new JToggleButton("H");
        JToggleButton btnS = new JToggleButton("S");
        JToggleButton btnB = new JToggleButton("B");
        DimensionsListener dimensionsListener = new DimensionsListener(dimensions, btnH, btnS, btnB);
        btnH.addActionListener(dimensionsListener);
        btnS.addActionListener(dimensionsListener);
        btnB.addActionListener(dimensionsListener);
        if (dimensions.hue) {
            btnH.setSelected(true);
        }
        if (dimensions.saturation) {
            btnS.setSelected(true);
        }
        if (dimensions.brightness) {
            btnB.setSelected(true);
        }
        dimensionsPanel.add(btnH);
        dimensionsPanel.add(btnS);
        dimensionsPanel.add(btnB);
        parametersMap.put("Dimensions", dimensionsPanel);

        JComboBox comparisonComboBox = new JComboBox(NumberComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(comparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        parametersMap.put("Comparison", comparisonComboBox);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(compareToLikenessPercentage, 0, 100, 1);
        JSpinner likenessPercentageSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        likenessPercentageSpinner.addChangeListener(new PercentageListener());
        parametersMap.put("Likeness in %", likenessPercentageSpinner);

        return parametersMap;
    }

    private class ComparisonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setComparisonOperator((NumberComparisonOperator) ((JComboBox) (e.getSource())).getSelectedItem());
        }
    }

    private class PercentageListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            setCompareToLikenessPercentage((Integer)((JSpinner)e.getSource()).getValue());
        }
    }
}
