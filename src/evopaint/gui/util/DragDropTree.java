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
package evopaint.gui.util;

import evopaint.pixel.rulebased.RuleSetCollection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author Craig Wood (http://www.coderanch.com/t/346509/Swing-AWT-SWT-JFace/java/JTree-drag-drop-inside-one)
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class DragDropTree extends JTree {

    public DragDropTree() {
        setDragEnabled(true);
        setDropMode(DropMode.ON);
        setTransferHandler(new TreeTransferHandler());
        getSelectionModel().setSelectionMode(
                TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
    }

    private JScrollPane getContent() {
        expandTree(this);
        return new JScrollPane(this);
    }

    private void expandTree(JTree tree) {
        DefaultMutableTreeNode root =
                (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) e.nextElement();
            if (node.isLeaf()) {
                continue;
            }
            int row = tree.getRowForPath(new TreePath(node.getPath()));
            tree.expandRow(row);
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new DragDropTree().getContent());
        f.setSize(400, 400);
        f.setLocation(200, 200);
        f.setVisible(true);
    }
}

class TreeTransferHandler extends TransferHandler {

    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    DefaultMutableTreeNode[] nodesToRemove;

    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=\""
                    + javax.swing.tree.DefaultMutableTreeNode[].class.getName()
                    + "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        support.setShowDropLocation(true);
        if (!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }

        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation) support.getDropLocation();
        JTree tree = (JTree) support.getComponent();
        //int dropRow = tree.getRowForPath(dl.getPath());
        //int[] selRows = tree.getSelectionRows();
        //for (int i = 0; i < selRows.length; i++) {
        //    if (selRows[i] == dropRow) {
        //        return false;
       //     }
       // }
        
        // forbid moving collection nodes because they will be reordered on
        // restart anyways
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode target =
                (DefaultMutableTreeNode) dest.getLastPathComponent();
        if (((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getAllowsChildren()) {
            return false;
        }

        // and forbid rule set nodes to be moved onto anything but collection nodes
        if (false == ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getAllowsChildren() &&
                false == target.getUserObject() instanceof RuleSetCollection) {
            return false;
        }
        

        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        //int action = support.getDropAction();
       // if (action == MOVE) {
       //     return haveCompleteNode(tree);
        //}

        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
       // TreePath path = tree.getPathForRow(selRows[0]);
       // DefaultMutableTreeNode firstNode =
       //         (DefaultMutableTreeNode) path.getLastPathComponent();
       // if (firstNode.getChildCount() > 0
       //         && target.getLevel() < firstNode.getLevel()) {
       //     return false;
       // }
        return true;
    }

    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first =
                (DefaultMutableTreeNode) path.getLastPathComponent();
        int childCount = first.getChildCount();
// first has children and no children are selected.
        if (childCount > 0 && selRows.length == 1) {
            return false;
        }
// first may have children.
        for (int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            DefaultMutableTreeNode next =
                    (DefaultMutableTreeNode) path.getLastPathComponent();
            if (first.isNodeChild(next)) {
// Found a child of first.
                if (childCount > selRows.length - 1) {
// Not all children of first are selected.
                    return false;
                }
            }
        }
        return true;
    }

    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {

            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<DefaultMutableTreeNode> copies =
                    new ArrayList<DefaultMutableTreeNode>();
            List<DefaultMutableTreeNode> toRemove =
                    new ArrayList<DefaultMutableTreeNode>();
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);

            for (int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next =
                        (DefaultMutableTreeNode) paths[i].getLastPathComponent();
// Do not allow higher level nodes to be added to list.
                if (next.getLevel() < node.getLevel()) {
                    break;
                } else if (next.getLevel() > node.getLevel()) { // child node
                    copy.add(copy(next));
// node already contains child
                } else { // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            DefaultMutableTreeNode[] nodes =
                    copies.toArray(new DefaultMutableTreeNode[copies.size()]);
            nodesToRemove =
                    toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    /** Defensive copy used in createTransferable. */
    private DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
        return new DefaultMutableTreeNode(node.getUserObject(), node.getAllowsChildren());
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        if ((action & MOVE) == MOVE) {
            JTree tree = (JTree) source;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
// Remove nodes saved in nodesToRemove in createTransferable.
            for (int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
    }

    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
// Extract transfer data.
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch (java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
// Get drop location info.
        JTree.DropLocation dl =
                (JTree.DropLocation) support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent =
                (DefaultMutableTreeNode) dest.getLastPathComponent();
        JTree tree = (JTree) support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
// Configure for drop mode.
        int index = childIndex; // DropMode.INSERT
        if (childIndex == -1) { // DropMode.ON
            index = parent.getChildCount();
        }
// Add data to model.
        for (int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {

        DefaultMutableTreeNode[] nodes;

        public NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}
