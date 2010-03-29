/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.World;
import evopaint.gui.ruleseteditor.util.NamedObjectListCellRenderer;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComboBox;
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
        String ret = "energy of ";
        ret += super.toString();
        ret += " ";
        ret += comparisonOperator.toString();
        ret += " ";
        ret += energyValue;
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

    public LinkedHashMap<String,JComponent> getParametersForGUI() {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();

        JComboBox comparisonComboBox = new JComboBox(NumberComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(comparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        ret.put("Comparison", comparisonComboBox);
       
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(energyValue, 0, Integer.MAX_VALUE, 1);
        JSpinner energyValueSpinner = new JSpinner(spinnerModel);
        energyValueSpinner.addChangeListener(new ValueListener());
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
