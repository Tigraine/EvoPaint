/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.interfaces.IRule;
import evopaint.pixel.RuleSet;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleList extends JPanel {

    public JRuleList(RuleSet ruleSet) {
        
        for (IRule rule : ruleSet.getRules()) {
            add(new JRule(rule));
        }
        
        // make me look pretty
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(new TitledBorder("Ruleset"));
    }
}
