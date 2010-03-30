/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.gui.util.DragDropList;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author tam
 */
public class JRuleList extends DragDropList {
    private DefaultListModel listModel;

    public List<IRule> getRules() {
        List<IRule> rules = new ArrayList<IRule>(listModel.capacity());
        for (int i = 0; i < listModel.size(); i++) {
            rules.add((IRule)listModel.get(i));
        }
        return rules;
    }

    public JRuleList(List<IRule> rules) {
        super(new DefaultListModel());
        listModel = (DefaultListModel)getModel();

        for (IRule rule : rules) {
            listModel.addElement(rule);
        }

        setModel(listModel);
        setCellRenderer(new RuleCellRenderer());
        setVisibleRowCount(15);
    }

    private class RuleCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {

            JLabel ret = (JLabel)super.getListCellRendererComponent(list, ((IRule)value).toHTML(), index, isSelected, cellHasFocus);
            ret.setText("<html>" + ret.getText() + "</html>");
            return ret;
        }
    }
}
