/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.RuleSet;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
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
            listModel.addElement(rule);
        }

        setModel(listModel);
        setCellRenderer(new RuleCellRenderer());
        setVisibleRowCount(15);

        
        // make me look pretty
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setAlignmentX(Component.CENTER_ALIGNMENT);
        //setBorder(new TitledBorder("Ruleset"));
        //set


    }

    private class RuleCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {

            JLabel ret = (JLabel)super.getListCellRendererComponent(list, ((IRule)value).toString(), index, isSelected, cellHasFocus);

            return ret;
        }
    }
}
