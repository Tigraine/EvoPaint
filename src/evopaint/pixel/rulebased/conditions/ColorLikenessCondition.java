/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.World;
import evopaint.gui.ruleseteditor.util.DimensionsListener;
import evopaint.gui.ruleseteditor.JRuleSetManager;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.gui.ruleseteditor.util.NamedObjectListCellRenderer;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
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

    public String getName() {
        return "Color Likeness";
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

    @Override
    public String toString() {
        String ret = new String();
        ret += getDirectionsString();
        ret += super.getDirections().size() > 1 ? " are " : " is ";
        ret += "colored ";
        ret += comparisonOperator.toString();
        ret += " ";
        ret += compareToLikenessPercentage;
        ret += "% like ";
        ret += comparedColor.toString();
        ret += " (dimensions: ";
        ret += dimensions.toString();
        ret += ")";
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = new String();
        ret += getDirectionsString();
        ret += super.getDirections().size() > 1 ? " are " : " is ";
        ret += "colored ";
        ret += comparisonOperator.toHTML();
        ret += " ";
        ret += compareToLikenessPercentage;
        ret += "% like ";
        ret += comparedColor.toHTML();
        ret += " <span style='color: #777777;'>(dimensions: ";
        ret += dimensions.toHTML();
        ret += ")</span>";
        return ret;
    }

    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            double distance = them.getPixelColor().distanceTo(comparedColor, dimensions);
            //System.out.println("distance: " + distance);
            int likenessPercentage = (int)((1 - distance) * 100);
            return comparisonOperator.compare(likenessPercentage, compareToLikenessPercentage);
        }
        return true;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI() {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();

        String colorString = "#" + Integer.toHexString(comparedColor.getInteger()).substring(2).toUpperCase();
        JButton colorButton = new JButton(colorString);
        colorButton.addMouseListener(new ColorListener(this, colorButton));
        //colorButton.setPreferredSize(new Dimension(50, 25));
        ret.put("Color", colorButton);

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
        ret.put("Dimensions", dimensionsPanel);

        JComboBox comparisonComboBox = new JComboBox(NumberComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(comparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        ret.put("Comparison", comparisonComboBox);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(compareToLikenessPercentage, 0, 100, 1);
        JSpinner likenessPercentageSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        likenessPercentageSpinner.addChangeListener(new PercentageListener());
        ret.put("Likeness in %", likenessPercentageSpinner);

        return ret;
    }

    public ColorLikenessCondition(List<RelativeCoordinate> directions, NumberComparisonOperator comparisonOperator, int compareToLikenessPercentage, ColorDimensions dimensions, PixelColor comparedColor) {
        super(directions);
        this.comparisonOperator = comparisonOperator;
        this.comparedColor = comparedColor;
        this.compareToLikenessPercentage = compareToLikenessPercentage;
        this.dimensions = dimensions;
    }

    public ColorLikenessCondition() {
        super(new ArrayList<RelativeCoordinate>(9));
        this.comparisonOperator = NumberComparisonOperator.EQUAL;
        this.comparedColor = new PixelColor(0, 0, 0);
        this.compareToLikenessPercentage = 0;
        this.dimensions = new ColorDimensions(true, true, true);
    }

    private class ColorListener implements MouseListener {
        ColorLikenessCondition colorLikenessCondition;
        private JButton colorButton;

        public ColorListener(ColorLikenessCondition colorLikenessCondition, JButton colorLabel) {
            this.colorLikenessCondition = colorLikenessCondition;
            this.colorButton = colorLabel;
        }

        public void mouseClicked(MouseEvent e) {
            Color color = new Color(comparedColor.getInteger());
            final JColorChooser colorChooser = new JColorChooser(color);
            colorChooser.setPreviewPanel(new JPanel());
            JDialog dialog = JColorChooser.createDialog(this.colorButton, "Choose Color", true,
                    colorChooser, new ColorChooserOKListener(this.colorLikenessCondition, colorChooser, this.colorButton), new ColorChooserCancelListener());
            dialog.pack();
            dialog.setVisible(true);
        }

        public void mousePressed(MouseEvent e) {}

        public void mouseReleased(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        private class ColorChooserOKListener implements ActionListener {
            ColorLikenessCondition colorLikenessCondition;
            JColorChooser colorChooser;
            JButton owner;

            public ColorChooserOKListener(ColorLikenessCondition colorLikenessCondition, JColorChooser colorChooser, JButton owner) {
                this.colorLikenessCondition = colorLikenessCondition;
                this.colorChooser = colorChooser;
                this.owner = owner;
            }

            public void actionPerformed(ActionEvent e) {
                Color c = colorChooser.getColor();
                Configuration config = ((JRuleSetManager) SwingUtilities.getWindowAncestor(this.owner)).getConfiguration();
                colorLikenessCondition.getComparedColor().setInteger(c.getRGB(), config.rng);
                owner.setText("#" + Integer.toHexString(comparedColor.getInteger()).substring(2).toUpperCase());
                owner.setForeground(c);
            }
        }

        private class ColorChooserCancelListener implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                // NOOP
            }
        }
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
