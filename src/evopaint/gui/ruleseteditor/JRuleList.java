/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.gui.util.DragDropList;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    public List<IRule> getRules() {
        DefaultListModel listModel = (DefaultListModel)getModel();
        List<IRule> rules = new ArrayList<IRule>(listModel.capacity());
        for (int i = 0; i < listModel.size(); i++) {
            rules.add((IRule)listModel.get(i));
        }
        return rules;
    }

    public void setRules(List<IRule> rules) {
        DefaultListModel listModel = (DefaultListModel)getModel();
        for (IRule rule : rules) {
            listModel.addElement(rule);
        }
    }

    public JRuleList(final JRuleSetManager jRuleSetManager) {
        super(new DefaultListModel());
        setCellRenderer(new RuleCellRenderer());
        setVisibleRowCount(5);

        // open rule editor on double click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = locationToIndex(e.getPoint());
                    DefaultListModel listModel = (DefaultListModel)getModel();
                    jRuleSetManager.openRuleEditor((IRule)listModel.get(index));
                 }
            }
        });
        setPreferredSize(new Dimension(100,100));
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
