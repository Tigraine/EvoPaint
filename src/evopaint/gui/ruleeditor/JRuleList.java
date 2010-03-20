/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.interfaces.IRule;
import java.awt.Component;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleList extends JPanel {

    public JRuleList(Collection<IRule> rules) {
        
        for (IRule rule :rules) {
            add(new JRule(rule));
        }
        
        // make me look pretty
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(new TitledBorder("Ruleset"));
    }
}
