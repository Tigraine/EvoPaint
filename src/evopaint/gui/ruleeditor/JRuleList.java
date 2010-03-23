/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.pixel.interfaces.IRule;
import evopaint.pixel.RuleSet;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author tam
 */
public class JRuleList extends JList {
    private DefaultListModel listModel;

    public JRuleList(RuleSet ruleSet) {
        listModel = new DefaultListModel();

        for (IRule rule : ruleSet.getRules()) {
            listModel.addElement(rule.toString());
        }

        setModel(listModel);
        setVisibleRowCount(5);

        
        // make me look pretty
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setAlignmentX(Component.CENTER_ALIGNMENT);
        //setBorder(new TitledBorder("Ruleset"));
        //set
    }
}
