/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.interfaces.ICondition;
import java.awt.GridLayout;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JConditionPool extends JPanel {

    public JConditionPool(Collection<ICondition> conditions) {
        for (ICondition condition : conditions) {
            add(new ConditionLabel(condition.toString()));
        }

        setBorder(new TitledBorder("Available Contitions"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private class ConditionLabel extends JLabel {

        public ConditionLabel(String text) {
            super(text);
            setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        }
    }

}
