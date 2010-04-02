/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.World;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.rulebased.util.ObjectComparisonOperator;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public class EmptyCondition extends AbstractCondition {

    private ObjectComparisonOperator comparisonOperator;

    public String getName() {
        return "Empty";
    }

    public ObjectComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(ObjectComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    @Override
    public String toString() {
        String ret = getDirectionsString();
        ret += super.getDirections().size() > 1 ? " are " : " is ";
        if (comparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
            ret += "not ";
        }
        ret += "empty";
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = getDirectionsString();
        ret += super.getDirections().size() > 1 ? " are " : " is ";
        if (comparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
            ret += "not ";
        }
        ret += "empty";
        return ret;
    }

    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            if (comparisonOperator.compare(them, null) == false) {
                return false; // so this is what lazy evaluation looks like...
            }
        }
        return true;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration) {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();
        
        JComboBox comparisonComboBox = new JComboBox(ObjectComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(comparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        comparisonComboBox.setPreferredSize(new Dimension(80, 25));
        ret.put("Comparison", comparisonComboBox);

        return ret;
    }

    public EmptyCondition(List<RelativeCoordinate> directions, ObjectComparisonOperator comparisonOperator) {
        super(directions);
        this.comparisonOperator = comparisonOperator;
    }

    public EmptyCondition() {
        super(new ArrayList<RelativeCoordinate>(9));
        this.comparisonOperator = ObjectComparisonOperator.EQUAL;
    }

    private class ComparisonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setComparisonOperator((ObjectComparisonOperator) ((JComboBox) (e.getSource())).getSelectedItem());
        }
    }

}
