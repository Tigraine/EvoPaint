/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.gui.rulesetmanager;

import evopaint.Configuration;
import evopaint.gui.util.DragDropList;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.CollectionNode;
import evopaint.util.RuleSetNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JRuleList extends JPanel implements TreeSelectionListener, ListDataListener, ListSelectionListener {
    private Configuration configuration;
    private DragDropList list;
    private DefaultListModel model;
    private RuleSetNode lastSelectedRuleSetNode;
    private boolean dirty;
    private JRuleSetTree tree;
    private JButton btnEdit;
    private JButton btnCopy;
    private JButton btnDelete;

    public boolean isDirty() {
        return dirty;
    }

    public void clean() {
        if (false == dirty) {
            return;
        }
        assert(lastSelectedRuleSetNode != null);

        // replace rules in rule set node
        RuleSet ruleSet = (RuleSet)lastSelectedRuleSetNode.getUserObject();
        ruleSet.setRules(getRules());
        lastSelectedRuleSetNode.setUserObject(ruleSet);

        // inform the tree listeners about the changes
        CollectionNode parentNode = (CollectionNode)lastSelectedRuleSetNode.getParent();
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        treeModel.nodesChanged(parentNode,
                new int [] {parentNode.getIndex(lastSelectedRuleSetNode)});

        dirty = false;
    }
    
    public List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(model.capacity());

        for (int i = 0; i < model.size(); i++) {
            rules.add((Rule)model.get(i));
        }
        return rules;
    }

    public void replaceSelectedRule(Rule rule) {
        model.set(list.getSelectedIndex(), rule);
        dirty = true;
    }

    public Rule getSelectedRule() {
        if (list.isSelectionEmpty()) {
            return null;
        }
        return (Rule)list.getSelectedValue();
    }

    public int locationToIndex(Point location) {
        return list.locationToIndex(location);
    }

    public void valueChanged(TreeSelectionEvent e) {

        // if this is a removal event of a dirty rule set, we don't care
        // or else we will reveive a nive NullPointException for trying...
        if (false == e.isAddedPath()) {
            dirty = false;
            return;
        }

        clean();

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
        Object userObject = node.getUserObject();

        if (userObject == null) {
            return;
        }

        if (false == (userObject instanceof RuleSet)) {
            return;
        }

        // save rule set node for writing it back to disc if changed
        lastSelectedRuleSetNode = (RuleSetNode)node;

        // save data listeners
        ListDataListener [] listeners = model.getListDataListeners();

        // create new model for rule set
        model = new DefaultListModel();

        RuleSet ruleSet = (RuleSet)userObject;
        for (Rule rule : ruleSet.getRules()) {
            model.addElement(rule);
        }

        // re-add data listeners
        for (ListDataListener l : listeners) {
            model.addListDataListener(l);
        }

        list.setModel(model);
    }

    public void contentsChanged(ListDataEvent e) {
        //System.out.println("dirty");
        dirty = true;
    }

    public void intervalAdded(ListDataEvent e) {
        //System.out.println("dirty");
        dirty = true;
    }

    public void intervalRemoved(ListDataEvent e) {
        //System.out.println("dirty");
        dirty = true;
    }

    public JRuleList(Configuration configuration, JRuleSetTree tree, ActionListener btnEditListener, MouseListener doubleClickListener) {
        this.configuration = configuration;
        this.dirty = false;
        this.tree = tree;
        tree.addTreeSelectionListener(this);

        setLayout(new BorderLayout());
        setBorder(new LineBorder(Color.GRAY));
        setBackground(Color.WHITE);

        model = new DefaultListModel();
        model.addListDataListener(this);
        list = new DragDropList(model);
        list.setBorder(null);
        list.setCellRenderer(new RuleCellRenderer());
        list.addMouseListener(doubleClickListener);
        list.addListSelectionListener(this);
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

        btnEdit = new JButton(new ImageIcon(getClass().getResource("icons/button-edit.png")));
        btnEdit.setToolTipText("Opens the selected rule in the Rule Editor");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(btnEditListener);

        JButton btnAdd = new JButton(new ImageIcon(getClass().getResource("icons/button-add.png")));
        btnAdd.setToolTipText("Adds a new rule and opens it in the Rule Editor");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Rule newRule = new Rule();
                model.addElement(newRule);
                list.setSelectedValue(newRule, true);
                btnEdit.doClick();
            }
        });
        controlPanel.add(btnAdd);

        controlPanel.add(btnEdit);

        btnCopy = new JButton(new ImageIcon(getClass().getResource("icons/button-copy.png")));
        btnCopy.setToolTipText("Copies the selected rule");
        btnCopy.setEnabled(false);
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (list.isSelectionEmpty()) {
                    return;
                }
                int index = list.getSelectedIndex();
                final Rule protoRule = (Rule)model.get(index);
                Rule newRule = new Rule(protoRule);
                model.addElement(newRule);
                
            }
        });
        controlPanel.add(btnCopy);

        btnDelete = new JButton(new ImageIcon(getClass().getResource("icons/button-delete.png")));
        btnDelete.setToolTipText("Deletes the selected rule");
        btnDelete.setEnabled(false);
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

    public void valueChanged(ListSelectionEvent e) {

        // user interaction is said to cause multiple selection events
        // this magic method sorts out uninteresting ones
        if (e.getValueIsAdjusting()) {
            return;
        }

        if (list.getSelectedIndex() == -1) { // no rule selected
            btnEdit.setEnabled(false);
            btnCopy.setEnabled(false);
            btnDelete.setEnabled(false);
            return;
        }

        btnEdit.setEnabled(true);
        btnCopy.setEnabled(true);
        btnDelete.setEnabled(true);
    }


    private class RuleCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {

            JLabel ret = (JLabel)super.getListCellRendererComponent(list, ((Rule)value).toHTML(), index, isSelected, cellHasFocus);
            ret.setText("<html>" + ret.getText() + "</html>");
            return ret;
        }
    }


}
