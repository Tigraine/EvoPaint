/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.ExampleRuleSetFactory;
import evopaint.pixel.rulebased.RuleSet;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author tam
 */
public class JRuleSetBrowser extends JTree {
    DefaultMutableTreeNode root;

    public void updateCollections() {
        File userDir = new JFileChooser().getFileSystemView().getDefaultDirectory();
        File home = new File(userDir, ".evopaint");
        if (home.exists() == false) {
            home.mkdir();
        }
        File collectionsDir = new File(home, "collections");
        if (collectionsDir.exists() == false) {
            collectionsDir.mkdir();
            createExampleCollections(collectionsDir);
        }
        File [] collectionDirs = collectionsDir.listFiles();

        root = new DefaultMutableTreeNode();
        try {
            for (File collectionDir : collectionDirs) {
                DefaultMutableTreeNode collectionNode = new DefaultMutableTreeNode(collectionDir.getName());
                File [] ruleSetFiles = collectionDir.listFiles();
                for (File ruleSetFile : ruleSetFiles) {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(ruleSetFile));
                    RuleSet ruleSet = (RuleSet)in.readObject();
                    in.close();
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
        setModel(model);
    }

    public void createExampleCollections(File dir) {
        RuleSet ruleSet = ExampleRuleSetFactory.createSimpleColorAssimilation();
        try {
            File collectionDir = new File(dir, "simple");
            collectionDir.mkdir();
            File ruleSetFile = new File(collectionDir, ruleSet.getName().replace(" ", "_").toLowerCase() + ".eprs");
            ruleSetFile.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruleSetFile));
            out.writeObject(ruleSet);
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        } 
    }

    public JRuleSetBrowser() {
        updateCollections();
        setRootVisible(false);
        setCellRenderer(new RuleSetTreeCellRenderer());
        setPreferredSize(new Dimension(250, 250));
    }

    class RuleSetTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
            if (userObject instanceof RuleSet) {
                return super.getTreeCellRendererComponent(tree, new DefaultMutableTreeNode(((RuleSet)userObject).getName()), sel, expanded, leaf, row, hasFocus);
            }
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }

    }
}
