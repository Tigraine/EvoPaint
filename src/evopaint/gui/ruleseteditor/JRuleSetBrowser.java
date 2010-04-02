/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.gui.ruleseteditor.util.NamedObjectListCellRenderer;
import evopaint.util.FileHandler;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author tam
 */
public class JRuleSetBrowser extends JPanel {
    JTree tree;
    DefaultTreeModel model;
    DefaultMutableTreeNode root;
    Component newDialogOwner;

    public JTree getTree() {
        return tree;
    }

    public boolean validateNonExistence(RuleSetCollection collection) {
        Enumeration children = root.children();
        while (children != null && children.hasMoreElements()) {
            CollectionNode collectionNode = (CollectionNode)children.nextElement();
            RuleSetCollection c = (RuleSetCollection)collectionNode.getUserObject();
            if (collection.getName().equals(c.getName())) {
                JOptionPane.showMessageDialog((JFrame)SwingUtilities.getWindowAncestor(newDialogOwner),
                    "A collection with this name already exists, why do you have to do this?\nAnyway, I did NOT save your collection...",
                    "I am angry with you!",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public JRuleSetBrowser(TreeSelectionListener treeSelectionListener) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.GRAY));

        tree = new JTree();
        tree.setRootVisible(false);
        tree.setExpandsSelectedPaths(true);
        tree.setShowsRootHandles(true);
        tree.setToggleClickCount(1);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new RuleSetTreeCellRenderer());
        tree.addTreeSelectionListener(treeSelectionListener);
        // don't set the preferred size! set the divider location instead
        // or else the scrollpane will scroll even if the tree is empty
        //setPreferredSize(new Dimension(250, 250));

        JScrollPane scrollPaneForTree = new JScrollPane(tree,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForTree.setBorder(null);
        scrollPaneForTree.setViewportBorder(null);

        // create buttons for control panel of browser
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(0xF2F2F5));
        JButton browserBtnNew = new JButton("New...");
        newDialogOwner = browserBtnNew;
        browserBtnNew.addActionListener(new BrowserBtnNewListener());
        controlPanel.add(browserBtnNew);
        JButton browserBtnCopy = new JButton("Copy");
        browserBtnCopy.addActionListener(new BtnCopyListener());
        controlPanel.add(browserBtnCopy);
        JButton browserBtnDelete = new JButton("Delete");
        browserBtnDelete.addActionListener(new BtnDeleteListener());
        controlPanel.add(browserBtnDelete);

        add(scrollPaneForTree, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // init tree
        FileHandler fh = FileHandler.getHandler();
        root = new DefaultMutableTreeNode();
        for (RuleSetCollection collection : fh.getCollections()) {
            CollectionNode collectionNode = new CollectionNode(collection);
            for (RuleSet ruleSet : collection.getRulesets()) {
                collectionNode.add(new RuleSetNode(ruleSet));
            }
            root.add(collectionNode);
        }
        model = new DefaultTreeModel(root);
        tree.setModel(model);
    }

    class RuleSetTreeCellRenderer extends DefaultTreeCellRenderer {

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

    private class BrowserBtnNewListener implements ActionListener {
        private JDialog dialog;
        private JRadioButton collectionRadio;
        private JRadioButton ruleSetRadio;
        private JComboBox comboBoxCollections;
        private JTextField nameField;

        public void actionPerformed(ActionEvent e) {
            final FileHandler fh = FileHandler.getHandler();

            dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(newDialogOwner), "Edit Action", true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setLayout(new GridBagLayout());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.insets = new Insets(10, 10, 10, 10);

            JPanel radioPanel = new JPanel();
            radioPanel.setBorder(new TitledBorder("Type"));
            collectionRadio = new JRadioButton("Collection");

            radioPanel.add(collectionRadio);
            ruleSetRadio = new JRadioButton("Rule Set");
            if (root.getChildCount() == 0) {
                ruleSetRadio.setEnabled(false);
            }
            radioPanel.add(ruleSetRadio);
            ButtonGroup group = new ButtonGroup();
            group.add(collectionRadio);
            group.add(ruleSetRadio);
            dialog.add(radioPanel, constraints);

            // setup combobox for collectin selection for rule set
            DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
            Enumeration children = root.children();
            while (children != null && children.hasMoreElements()) {
                comboBoxModel.addElement(
                        ((RuleSetCollection)
                        ((CollectionNode)children.nextElement()).getUserObject()));
            }
            comboBoxCollections = new JComboBox(comboBoxModel);
            comboBoxCollections.setRenderer(new NamedObjectListCellRenderer());
            final JPanel comboBoxWrapper = new JPanel(new GridLayout());
            comboBoxWrapper.setBorder(new TitledBorder("Add to Collection"));
            comboBoxWrapper.add(comboBoxCollections);
            comboBoxWrapper.setVisible(false);
            constraints.gridy = 1;
            dialog.add(comboBoxWrapper, constraints);

            // intelligent preset
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if (selectedNode == null) {
                collectionRadio.setSelected(true);
                comboBoxWrapper.setVisible(false);
            } else if (selectedNode instanceof CollectionNode) {
                comboBoxCollections.setSelectedItem((RuleSetCollection)selectedNode.getUserObject());
                ruleSetRadio.setSelected(true);
                comboBoxWrapper.setVisible(true);
            } else if (selectedNode instanceof RuleSetNode) {
                CollectionNode collectionNode = (CollectionNode)selectedNode.getParent();
                comboBoxCollections.setSelectedItem((RuleSetCollection)collectionNode.getUserObject());
                ruleSetRadio.setSelected(true);
                comboBoxWrapper.setVisible(true);
            } else {
                assert(false);
            }

            collectionRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (comboBoxWrapper.isVisible()) {
                        comboBoxWrapper.setVisible(false);
                        dialog.pack();
                    }
                }
            });

            ruleSetRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (false == comboBoxWrapper.isVisible()) {
                        comboBoxWrapper.setVisible(true);
                        dialog.pack();
                    }
                }
            });

            // name field
            JPanel nameFieldWrapper = new JPanel(new GridLayout());
            nameFieldWrapper.setBorder(new TitledBorder("Name"));
            nameField = new JTextField();
            nameFieldWrapper.add(nameField);
            constraints.gridy = 2;
            dialog.add(nameFieldWrapper, constraints);

            // and the control panel
            JPanel controlPanel = new JPanel();
            final JButton btnOK = new JButton("OK");
            btnOK.addActionListener(new newDialogOKListener());
            controlPanel.add(btnOK);
            final JButton btnCancel = new JButton("Cancel");
            btnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
             });
            controlPanel.add(btnCancel);
            constraints.gridy = 3;
            dialog.add(controlPanel, constraints);

            dialog.pack();
            dialog.setLocationRelativeTo(newDialogOwner);
            dialog.setVisible(true);
        }

        private class newDialogOKListener implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                FileHandler fh = FileHandler.getHandler();

                // creating a collection
                if (collectionRadio.isSelected()) {
                    
                    RuleSetCollection collection = new RuleSetCollection(
                            nameField.getText(),
                            "I am a sad collection, because I have no description but you can brighten up my day by clicking 'Edit' to describe me.",
                                new ArrayList());

                    // check whether a collection with this name already exists
                    if (false == validateNonExistence(collection)) {
                        return;
                    }

                    CollectionNode collectionNode = new CollectionNode(collection);
                    model.insertNodeInto(collectionNode,
                            root, root.getChildCount());

                    // expand it
                    TreePath collectionPath = new TreePath(collectionNode.getPath());
                    tree.expandPath(collectionPath);
                    // select it
                    tree.setSelectionPath(collectionPath);
                    // and make it visible in case we have to scroll
                    tree.scrollPathToVisible(collectionPath);

                    fh.writeCollection(collection);

                    return;
                }

                // creating a rule set
                RuleSet ruleSet = new RuleSet(nameField.getText(),
                        "These rules are made for ruling, that's just what they'll do. One of these days these rule are gonna rule all over you.",
                     new ArrayList<IRule>());

                // add rule set to collection in memory
                RuleSetCollection collection = (RuleSetCollection)comboBoxCollections.getSelectedItem();

                for (RuleSet ruleSet1 : collection.getRulesets()) {
                    if (ruleSet.getName().equals(ruleSet1.getName())) {
                        JOptionPane.showMessageDialog((JFrame)SwingUtilities.getWindowAncestor(newDialogOwner),
                            "A rule set with this name already exists, why do you have to do this?\nAnyway, I did NOT create your rule set...",
                            "I am angry with you!",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                collection.getRulesets().add(ruleSet);

                // .. to collection in tree
                Enumeration children = root.children();
                assert(children != null);
                while (children.hasMoreElements()) {
                    CollectionNode collectionNode = (CollectionNode)children.nextElement();
                    if (collectionNode.getUserObject() == collection) {
                        RuleSetNode ruleSetNode = new RuleSetNode(ruleSet);
                        model.insertNodeInto(ruleSetNode,
                            collectionNode, collectionNode.getChildCount());
                        // expand it
                        TreePath collectionPath = new TreePath(collectionNode.getPath());
                        tree.expandPath(collectionPath);
                        // select it
                        TreePath ruleSetPath = new TreePath(ruleSetNode.getPath());
                        tree.setSelectionPath(ruleSetPath);
                        // and make it visible in case we have to scroll
                        tree.scrollPathToVisible(ruleSetPath);
                        break;
                    }
                }

                fh.writeCollection(collection);
            }
        }
    }

    private class BtnCopyListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
                       tree.getLastSelectedPathComponent();
            
            if (selectedNode == null) {
                return;
            }

             // collection copy
            if (selectedNode.getLevel() == 1) {
                RuleSetCollection collection =
                        (RuleSetCollection)selectedNode.getUserObject();
                RuleSetCollection newCollection = collection.getCopy();

                // make sure the name of the copy is unique
                boolean found = true;
                for (int i = 1; found == true; i++) {
                    newCollection.setName(collection.getName() + " (" + i + ")");
                    found = false;
                    Enumeration siblingCollectionNodes = root.children();
                    while (siblingCollectionNodes.hasMoreElements()) {
                        CollectionNode node = (CollectionNode)siblingCollectionNodes.nextElement();
                        RuleSetCollection rsc = (RuleSetCollection)node.getUserObject();
                        if (rsc.getName().equals(newCollection.getName())) {
                            found = true;
                            break;
                        }
                    }
                }

                // add collection with copied rule sets to tree
                CollectionNode newNode = new CollectionNode(newCollection);
                for (RuleSet ruleSet : collection.getRulesets()) {
                    newNode.insert(new RuleSetNode(ruleSet), newNode.getChildCount());
                }
                ((DefaultTreeModel)tree.getModel()).insertNodeInto(newNode,
                        root, root.getChildCount());
                // collapse the old selection
                tree.collapsePath(new TreePath(selectedNode.getPath()));
                // select it
                TreePath newNodePath = new TreePath(newNode.getPath());
                tree.setSelectionPath(newNodePath);
                // expand it
                tree.expandPath(newNodePath);
                // and make it so that the last rule set becomes scrolled to
                tree.scrollPathToVisible(new TreePath(newNode.getPath()));

                // write collection to file
                FileHandler fh = FileHandler.getHandler();
                fh.writeCollection(newCollection);

                return;
            }

            // else rule set copy
            CollectionNode collectionNode = (CollectionNode)selectedNode.getParent();
            RuleSetCollection collection = (RuleSetCollection)
                    collectionNode.getUserObject();
            RuleSet ruleSet = (RuleSet)selectedNode.getUserObject();
            RuleSet newRuleSet = ruleSet.getCopy();

            // make sure the name of the copy is unique
            boolean found = true;
            for (int i = 1; found == true; i++) {
                newRuleSet.setName(ruleSet.getName() + " (" + i + ")");
                found = false;
                Enumeration siblingRuleSetNodes = collectionNode.children();
                while (siblingRuleSetNodes.hasMoreElements()) {
                    RuleSetNode node = (RuleSetNode)
                            siblingRuleSetNodes.nextElement();
                    RuleSet rs = (RuleSet)node.getUserObject();
                    if (rs.getName().equals(newRuleSet.getName())) {
                        found = true;
                        break;
                    }
                }
            }

            // add rule set to collection
            collection.getRulesets().add(newRuleSet);

            // add rule set to tree,
            RuleSetNode newNode = new RuleSetNode(newRuleSet);
            ((DefaultTreeModel)tree.getModel()).insertNodeInto(newNode,
                    collectionNode, collectionNode.getChildCount());
            // select it
            TreePath newNodePath = new TreePath(newNode.getPath());
            tree.setSelectionPath(newNodePath);
            // and make it visible in case we have to scroll
            tree.scrollPathToVisible(newNodePath);
            


            // write collection with new rule set to file
            FileHandler fh = FileHandler.getHandler();
            fh.writeCollection(collection);

            return;
        }
    }

    private class BtnDeleteListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
                       tree.getLastSelectedPathComponent();

            if (selectedNode == null) {
                return;
            }

            // select the node before this one
            DefaultMutableTreeNode nodeBefore =
                    (DefaultMutableTreeNode)selectedNode.getPreviousSibling();
            if (nodeBefore != null) {
                tree.setSelectionPath(new TreePath(nodeBefore.getPath()));
            } else {
                // or after, if it is the first in a (sub)tree
                DefaultMutableTreeNode nodeAfter =
                    (DefaultMutableTreeNode)selectedNode.getNextSibling();
                if (nodeAfter != null) {
                    tree.setSelectionPath(new TreePath(nodeAfter.getPath()));
                }
            }

            final Object o = selectedNode.getUserObject();

            final FileHandler fh = FileHandler.getHandler();
            if (o instanceof RuleSetCollection) { // collection
                final RuleSetCollection collection = (RuleSetCollection)selectedNode.getUserObject();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                            fh.deleteCollection(collection);
                    }
                });
            } else { // rule set
                CollectionNode collectionNode = (CollectionNode)selectedNode.getParent();
                final RuleSetCollection collection = (RuleSetCollection)collectionNode.getUserObject();
                RuleSet ruleSet = (RuleSet)o;
                collection.getRulesets().remove(ruleSet);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        fh.writeCollection(collection);
                    }
                });
            }

            // remove node from tree
            model.removeNodeFromParent(selectedNode);
        }
    }
    
    private class CollectionNode extends DefaultMutableTreeNode {

            public CollectionNode(Object userObject) {
                super(userObject, true);
            }

            @Override
            public boolean isLeaf() {
                return false;
            }
    }

    private class RuleSetNode extends DefaultMutableTreeNode {

            public RuleSetNode(Object userObject) {
                super(userObject, false);
            }

            @Override
            public boolean isLeaf() {
                return true;
            }
    }
}
