/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

import evopaint.Configuration;
import evopaint.gui.util.DragDropList;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.interfaces.IRule;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author tam
 */
public class JRuleList extends JPanel implements TreeSelectionListener, ListDataListener {
    private Configuration configuration;
    private DragDropList list;
    private DefaultListModel model;
    private RuleSetNode lastSelectedRuleSetNode;
    private boolean dirty;
    private JRuleSetTree tree;

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
    
    public List<IRule> getRules() {
        List<IRule> rules = new ArrayList<IRule>(model.capacity());

        for (int i = 0; i < model.size(); i++) {
            rules.add((IRule)model.get(i));
        }
        return rules;
    }

    public void replaceSelectedRule(IRule rule) {
        model.set(list.getSelectedIndex(), rule);
        dirty = true;
    }

    public IRule getSelectedRule() {
        if (list.isSelectionEmpty()) {
            return null;
        }
        return (IRule)list.getSelectedValue();
    }

    public int locationToIndex(Point location) {
        return list.locationToIndex(location);
    }

    public void valueChanged(TreeSelectionEvent e) {
        clean();

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
        Object userObject = node.getUserObject();

        if (userObject == null) {
            return;
        }

        if (false == (userObject instanceof RuleSet)) {
            return;
        }

        // save collection node for writing it back to disc if changed
        lastSelectedRuleSetNode = (RuleSetNode)node;

        // save data listeners
        ListDataListener [] listeners = model.getListDataListeners();

        // create new model for rule set
        model = new DefaultListModel();

        RuleSet ruleSet = (RuleSet)userObject;
        for (IRule rule : ruleSet.getRules()) {
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
