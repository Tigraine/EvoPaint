/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.pixel.misc.IntegerComparisonOperator;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author tam
 */
public class ComparisonOperationsComboBoxModel extends DefaultComboBoxModel {

    public ComparisonOperationsComboBoxModel() {
        addElement(IntegerComparisonOperator.GREATER_THAN);
        addElement(IntegerComparisonOperator.LESS_THAN);
        addElement(IntegerComparisonOperator.EQUAL);
        addElement(IntegerComparisonOperator.GREATER_OR_EQUAL);
        addElement(IntegerComparisonOperator.LESS_OR_EQUAL);
    }

}
