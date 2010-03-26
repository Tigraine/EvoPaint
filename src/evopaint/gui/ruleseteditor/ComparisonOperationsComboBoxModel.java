/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.NumberComparisonOperator;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author tam
 */
public class ComparisonOperationsComboBoxModel extends DefaultComboBoxModel {

    public ComparisonOperationsComboBoxModel() {
        addElement(NumberComparisonOperator.GREATER_THAN);
        addElement(NumberComparisonOperator.LESS_THAN);
        addElement(NumberComparisonOperator.EQUAL);
        addElement(NumberComparisonOperator.GREATER_OR_EQUAL);
        addElement(NumberComparisonOperator.LESS_OR_EQUAL);
    }

}
