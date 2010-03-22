/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.conditions;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author tam
 */
public class NumberConditionComponent extends SpinnerNumberModel {

    public NumberConditionComponent(int value, int minimum, int maximum, int stepSize) {
        super(value, minimum, maximum, stepSize);
    }

    public JComponent createJComponent() {
        return new JSpinner(this);
    }
}
