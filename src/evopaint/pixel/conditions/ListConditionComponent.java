/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.conditions;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public class ListConditionComponent extends DefaultComboBoxModel {
    private List<ListConditonComponentElement> elements;

    public ListConditionComponent(Object[] items) {
        super(items);
    }

    public ListConditionComponent(List<String> elements) {
        for(String element : elements) {
            addElement(element);
        }
    }

    public JComponent createJComponent() {
        JComboBox jComboBox = new JComboBox();
        jComboBox.setModel(this);
    }
}
