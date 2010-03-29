/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.util;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author tam
 */
public class AutoSelectOnFocusSpinner extends JSpinner {

    public AutoSelectOnFocusSpinner(SpinnerModel model) {
        super(model);
        final JFormattedTextField spinnerText = ((JSpinner.DefaultEditor)getEditor()).getTextField();
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
        // some spinners are getting way too big because Integer.MAX_VALUE
        // can get pretty long, in this case we will truncate it a little to save space
        if (getPreferredSize().width > 90) {
            setPreferredSize(new Dimension(90, getPreferredSize().height));
        }
    }
}
