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

import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.CollectionNode;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JRuleSetTree extends JTree { // implements TreeModelListener {

    public JRuleSetTree(DefaultTreeModel model, MouseListener mouseListener) {
        setRootVisible(false);
        // setExpandsSelectedPaths(true); // broken POS (just does not work)
        setShowsRootHandles(true);
        setToggleClickCount(1);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setCellRenderer(new RuleSetTreeCellRenderer());
        setModel(model);
        // model.addTreeModelListener(this); // broken POS2 (will do a lot of
        // stuff, eg. not expand, not scroll, but when you reload the model
        // before you call the mentioned methods, it will cause empty lines and
        // even nullpointer exceptions in the UI

        addMouseListener(mouseListener);
    }

    // workaround for broken POS2
    public void updateVisibleInsert(DefaultMutableTreeNode node) {
        TreePath selectionPath = new TreePath(node.getPath());
        setSelectionPath(selectionPath);
        expandPath(selectionPath); // workaround for broken POS1
        scrollPathToVisible(selectionPath);
    }

    // workaround for broken POS2
    public void updateVisibleRemove(DefaultMutableTreeNode parentNode, int removedChildIndex) {
        DefaultMutableTreeNode selectedNode = null;
        if (parentNode.getChildCount() > 0) { // have other children to select?
            if (removedChildIndex < parentNode.getChildCount()) { // have childdren after removed one?
                selectedNode = (DefaultMutableTreeNode)parentNode.getChildAt(removedChildIndex);
            } else {
                selectedNode = (DefaultMutableTreeNode)parentNode.getChildAt(removedChildIndex - 1);
            }
        } else {
            selectedNode = parentNode;
        }
        setSelectionPath(new TreePath(selectedNode.getPath()));
    }

    public boolean isUniqueSiblingName(TreeNode parentNode, Object original, String name) {
        Enumeration enumeration = parentNode.children();
        assert(enumeration != null);
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)enumeration.nextElement();
            INamed namedObject = (INamed)node.getUserObject();
            if (original != namedObject &&
                    name.equals(namedObject.getName())) {
                showNameExistsError();
                return false;
            }
        }
        return true;
    }

    private void showNameExistsError() {
        JOptionPane.showMessageDialog((JFrame)SwingUtilities.getWindowAncestor(this),
                "Are you trying to make me crash saving stuff with already existing names?\nThis is not going to happen, baby... not going to happen at all...\nNow go and choose a different name, mkay? We good now.",
                "I am angry with you!",
                JOptionPane.ERROR_MESSAGE);
    }

/* code for broken POS2
    public void treeNodesChanged(TreeModelEvent e) {
    }

    public void treeNodesInserted(TreeModelEvent e) {
        DefaultMutableTreeNode newNode = (DefaultMutableTreeNode)e.getChildren()[0];
        TreePath selectionPath = new TreePath(newNode.getPath());
        setSelectionPath(selectionPath); // select the new item (and expand it - setExpandsSelectedPaths(true))
        scrollPathToVisible(selectionPath); // make it visible in case we have to scroll
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        int removedAt = e.getChildIndices()[0];
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
        DefaultMutableTreeNode selectedNode = null;
        if (parentNode.getChildCount() > 0) { // have other children to select?
            if (removedAt < parentNode.getChildCount()) { // have childdren after removed one?
                selectedNode = (DefaultMutableTreeNode)parentNode.getChildAt(removedAt);
            } else {
                selectedNode = (DefaultMutableTreeNode)parentNode.getChildAt(removedAt - 1);
            }
        } else {
            selectedNode = parentNode;
        }
        setSelectionPath(new TreePath(selectedNode.getPath()));
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }
 */

    private class RuleSetTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
            if (userObject instanceof RuleSet) {
                return super.getTreeCellRendererComponent(tree, new DefaultMutableTreeNode(((RuleSet)userObject).getName()), sel, expanded, leaf, row, hasFocus);
            }
            if (userObject instanceof RuleSetCollection) {
                 return super.getTreeCellRendererComponent(tree, new DefaultMutableTreeNode(((RuleSetCollection)userObject).getName()), sel, expanded, leaf, row, hasFocus);
            }
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }


}
