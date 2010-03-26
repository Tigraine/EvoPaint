/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author tam
 */
public class JCondition extends JPanel {

    public JCondition(ICondition condition, DefaultComboBoxModel availableConditions) {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        JTargetPicker targetPicker = new JTargetPicker();
        add(targetPicker);

        JComboBox comboBoxConditions = new JComboBox(availableConditions);
        add(comboBoxConditions);
    }    
}
