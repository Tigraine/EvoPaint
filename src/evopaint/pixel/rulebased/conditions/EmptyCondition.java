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

    private ObjectComparisonOperator objectComparisonOperator;

    public String getName() {
        return "Empty";
    }

    public ObjectComparisonOperator getComparisonOperator() {
        return objectComparisonOperator;
    }

    public void setComparisonOperator(ObjectComparisonOperator comparisonOperator) {
        this.objectComparisonOperator = comparisonOperator;
    }

    @Override
    public String toString() {
        String ret = getDirectionsString();
        ret += " are ";
        if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
            ret += "not ";
        }
        ret += "empty";
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = getDirectionsString();
        ret += " are ";
        if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
            ret += "not ";
        }
        ret += "empty";
        return ret;
    }

    protected boolean isMetCallback(Pixel us, Pixel them) {
        return objectComparisonOperator.compare(them, null);
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration) {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();
        
        JComboBox comparisonComboBox = new JComboBox(ObjectComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(objectComparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        comparisonComboBox.setPreferredSize(new Dimension(80, 25));
        ret.put("Comparison", comparisonComboBox);

        return ret;
    }

    public EmptyCondition(int min, int max, List<RelativeCoordinate> directions, ObjectComparisonOperator objectComparisonOperator) {
        super(min, max, directions);
        this.objectComparisonOperator = objectComparisonOperator;
    }


    public EmptyCondition() {
        super(0, 0, new ArrayList<RelativeCoordinate>(9));
        this.objectComparisonOperator = ObjectComparisonOperator.EQUAL;
    }

    private class ComparisonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setComparisonOperator((ObjectComparisonOperator) ((JComboBox) (e.getSource())).getSelectedItem());
        }
    }

}
