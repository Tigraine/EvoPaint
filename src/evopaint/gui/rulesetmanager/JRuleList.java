/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

import evopaint.gui.util.DragDropList;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 *
 * @author tam
 */
public class JRuleList extends JPanel {
    private DragDropList list;
    private DefaultListModel model;

    public DefaultListModel getModel() {
        return model;
    }

    public DragDropList getList() {
        return list;
    }

    public List<IRule> getRules() {
        List<IRule> rules = new ArrayList<IRule>(model.capacity());
        for (int i = 0; i < model.size(); i++) {
            rules.add((IRule)model.get(i));
        }
        return rules;
    }

    public void setRules(List<IRule> rules) {
        model.clear();
        for (IRule rule : rules) {
            model.addElement(rule);
        }
    }

    public JRuleList(ActionListener btnEditListener, MouseListener doubleClickListener) {
        setLayout(new BorderLayout());
        setBorder(new LineBorder(Color.GRAY));
        setBackground(Color.WHITE);

        model = new DefaultListModel();
        list = new DragDropList(model);
        list.setBorder(null);
        list.setCellRenderer(new RuleCellRenderer());
        list.addMouseListener(doubleClickListener);
        list.setSelectionForeground(Color.BLACK);
        list.setSelectionBackground(new Color(0xe9eff8));
        // DO NOT GIVE THIS LIST A PREFERRED SIZE, IT WILL FUCK UP THE H-SCROLLBAR
        // DON'T: // ruleList.setPreferredSize(new Dimension(100,100));
        final JScrollPane scrollPaneForRuleList = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleList.setBorder(null);
        scrollPaneForRuleList.setViewportBorder(null);
        add(scrollPaneForRuleList, BorderLayout.CENTER);

        final JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(0xF2F2F5));
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        controlPanel.add(btnSave);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IRule newRule = new Rule();
                model.addElement(newRule);
            }
        });
        controlPanel.add(btnAdd);

        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(btnEditListener);
        controlPanel.add(btnEdit);

        JButton btnCopy = new JButton("Copy");
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (list.isSelectionEmpty()) {
                    return;
                }
                int index = list.getSelectedIndex();
                final IRule protoRule = (IRule)model.get(index);
                IRule newRule = (IRule)protoRule.getCopy();
                model.addElement(newRule);
                
            }
        });
        controlPanel.add(btnCopy);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (list.isSelectionEmpty()) {
                    return;
                }
                int index = list.getSelectedIndex();
                model.remove(index);
                if (index > 0) {
                    list.setSelectedIndex(index - 1);
                } else if (model.size() > 0) {
                    list.setSelectedIndex(0);
                }
            }
        });
        controlPanel.add(btnDelete);
        
        add(controlPanel, BorderLayout.SOUTH);
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
