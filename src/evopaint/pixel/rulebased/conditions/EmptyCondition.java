/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
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

    public EmptyCondition(int min, int max, List<RelativeCoordinate> directions, ObjectComparisonOperator objectComparisonOperator) {
        super("empty", min, max, directions);
        this.objectComparisonOperator = objectComparisonOperator;
    }


    public EmptyCondition() {
        super("empty", 0, 0, new ArrayList<RelativeCoordinate>(9));
        this.objectComparisonOperator = ObjectComparisonOperator.EQUAL;
    }

    public ObjectComparisonOperator getComparisonOperator() {
        return objectComparisonOperator;
    }

    public void setComparisonOperator(ObjectComparisonOperator comparisonOperator) {
        this.objectComparisonOperator = comparisonOperator;
    }

    protected boolean isMetCallback(Pixel us, Pixel them) {
        return objectComparisonOperator.compare(them, null);
    }

    public String toStringCallback(String conditionString) {
        conditionString += "are ";
        if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
            conditionString += "not ";
        }
        conditionString += "empty";
        return conditionString;
    }

    public String toHTMLCallback(String conditionString) {
        conditionString += "are ";
        if (objectComparisonOperator == ObjectComparisonOperator.NOT_EQUAL) {
            conditionString += "not ";
        }
        conditionString += "empty";
        return conditionString;
    }

    public LinkedHashMap<String,JComponent> parametersCallbackGUI(LinkedHashMap<String,JComponent> parametersMap) {
        JComboBox comparisonComboBox = new JComboBox(ObjectComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        comparisonComboBox.setSelectedItem(objectComparisonOperator);
        comparisonComboBox.addActionListener(new ComparisonListener());
        comparisonComboBox.setPreferredSize(new Dimension(80, 25));
        parametersMap.put("Comparison", comparisonComboBox);

        return parametersMap;
    }

    private class ComparisonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setComparisonOperator((ObjectComparisonOperator) ((JComboBox) (e.getSource())).getSelectedItem());
        }
    }
}
