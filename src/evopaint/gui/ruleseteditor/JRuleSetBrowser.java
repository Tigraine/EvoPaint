/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.ExampleRuleSetCollectionFactory;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author tam
 */
public class JRuleSetBrowser extends JPanel {
    JTree tree;

    public JTree getTree() {
        return tree;
    }

    public void updateCollections() {
        FileHandler fh = FileHandler.getHandler();
        File collectionsDir = fh.getCollectionsDir();
        File [] collectionFiles = collectionsDir.listFiles();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        try {
            for (File collectionFile : collectionFiles) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(collectionFile));
                RuleSetCollection ruleSetCollection = (RuleSetCollection)in.readObject();
                in.close();
                DefaultMutableTreeNode collectionNode = new DefaultMutableTreeNode(ruleSetCollection);
                for (RuleSet ruleSet : ruleSetCollection.getRulesets()) {
                    collectionNode.add(new DefaultMutableTreeNode(ruleSet));
                }
                root.add(collectionNode);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
    }


    public JRuleSetBrowser(TreeSelectionListener treeSelectionListener) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.GRAY));

        tree = new JTree();
        tree.setRootVisible(false);
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
        JButton browserBtnAdd = new JButton("Add");
        browserBtnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        controlPanel.add(browserBtnAdd);
        JButton browserBtnCopy = new JButton("Copy");
        browserBtnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        controlPanel.add(browserBtnCopy);
        JButton browserBtnDelete = new JButton("Delete");
        browserBtnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        controlPanel.add(browserBtnDelete);

        add(scrollPaneForTree, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        updateCollections();
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
}
