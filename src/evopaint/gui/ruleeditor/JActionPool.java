/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.interfaces.IAction;
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
public class JActionPool extends JPanel {

    public JActionPool(Collection<IAction> actions) {
        for (IAction action : actions) {
            add(new ActionLabel(action.toString()));
        }

        setBorder(new TitledBorder("Available Actions"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private class ActionLabel extends JLabel {

        public ActionLabel(String text) {
            super(text);
            setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        }
    }

}
