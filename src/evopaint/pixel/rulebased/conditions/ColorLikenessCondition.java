/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.gui.rulesetmanager.util.DimensionsListener;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.gui.rulesetmanager.util.ColorChooserLabel;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
 * @author tam
 */
public class ColorLikenessCondition extends AbstractCondition {

    private PixelColor comparedColor;
    private ColorDimensions dimensions;
    private int compareToLikenessPercentage;
    private NumberComparisonOperator comparisonOperator;

    public ColorLikenessCondition(int min, int max, List<RelativeCoordinate> directions, PixelColor comparedColor, ColorDimensions dimensions, int compareToLikenessPercentage, NumberComparisonOperator comparisonOperator) {
        super("color likeness", min, max, directions);
        this.comparedColor = comparedColor;
        this.dimensions = dimensions;
        this.compareToLikenessPercentage = compareToLikenessPercentage;
        this.comparisonOperator = comparisonOperator;
    }

    public ColorLikenessCondition() {
        super("color likeness", 0, 0, new ArrayList<RelativeCoordinate>(9));
        this.comparisonOperator = NumberComparisonOperator.EQUAL;
        this.comparedColor = new PixelColor(0, 0, 0);
        this.compareToLikenessPercentage = 0;
        this.dimensions = new ColorDimensions(true, true, true);
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

    protected boolean isMetCallback(Pixel us, Pixel them) {
        if (them == null) { // never forget to skip empty spots
            return false;
        }
        double distance = them.getPixelColor().distanceTo(comparedColor, dimensions);
        //System.out.println("distance: " + distance);
        int likenessPercentage = (int)((1 - distance) * 100);
        return comparisonOperator.compare(likenessPercentage, compareToLikenessPercentage);
    }

    public String toStringCallback(String conditionString) {
        conditionString += "are ";
        conditionString += "colored ";
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

    public String toHTMLCallback(String conditionString) {
        conditionString += "are ";
        conditionString += "colored ";
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
    
    public LinkedHashMap<String,JComponent> parametersCallbackGUI(LinkedHashMap<String,JComponent> parametersMap) {    
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
