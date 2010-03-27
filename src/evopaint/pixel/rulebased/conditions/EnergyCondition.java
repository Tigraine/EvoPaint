/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.pixel.rulebased.NumberComparisonOperator;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author tam
 */
public class EnergyCondition extends AbstractCondition {
    private static final String NAME = "energy";

    private NumberComparisonOperator comparisonOperator;
    private int energyValue;

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
        return NAME;
    }

    @Override
    public String toString() {
        String ret = "energy of ";
        ret += super.toString();
        ret += " ";
        ret += comparisonOperator.toString();
        ret += " ";
        ret += energyValue;
        ret += "?";
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

    public JPanel createParametersPanel() {
        JPanel ret = new JPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

        Box a = Box.createHorizontalBox();
        a.add(new JLabel("Comparison:"));
        a.add(Box.createHorizontalGlue());
        ret.add(a);
        JComboBox comparisonComboBox = new JComboBox(NumberComparisonOperator.createComboBoxModel());
        comparisonComboBox.setSelectedItem(comparisonOperator.toString());
        comparisonComboBox.setPreferredSize(new Dimension(50, 25));
        ret.add(comparisonComboBox);
       
        Box b = Box.createHorizontalBox();
        b.add(new JLabel("Value:"));
        b.add(Box.createHorizontalGlue());
        ret.add(b);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(energyValue, 0, Integer.MAX_VALUE, 1);
        JSpinner energyValueSpinner = new JSpinner(spinnerModel);
        energyValueSpinner.setPreferredSize(new Dimension(50, 25));
        final JFormattedTextField spinnerText = ((JSpinner.DefaultEditor)energyValueSpinner.getEditor()).getTextField();

        spinnerText.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() { // only seems to work this way
                        public void run() {
                            spinnerText.selectAll();
                        }
                });
            }

            public void focusLost(FocusEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        energyValueSpinner.grabFocus();
        ret.add(energyValueSpinner);

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
}
