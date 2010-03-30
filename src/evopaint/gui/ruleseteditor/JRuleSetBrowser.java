/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author tam
 */
public class JRuleSetBrowser extends JTree {

    public JRuleSetBrowser() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        
        DefaultMutableTreeNode collection = new DefaultMutableTreeNode("conway's game of life");
        collection.add(new DefaultMutableTreeNode("life pixel"));
        root.add(collection);

        collection = new DefaultMutableTreeNode("rainbows made of love");
        collection.add(new DefaultMutableTreeNode("vertical love"));
        collection.add(new DefaultMutableTreeNode("horizontal love"));
        collection.add(new DefaultMutableTreeNode("love bomb"));
        root.add(collection);

        collection = new DefaultMutableTreeNode("suddenly, ants");
        collection.add(new DefaultMutableTreeNode("hive"));
        collection.add(new DefaultMutableTreeNode("ressource"));
        collection.add(new DefaultMutableTreeNode("ant"));
        collection.add(new DefaultMutableTreeNode("pheromone"));
        root.add(collection);

        DefaultTreeModel model = new DefaultTreeModel(root);
        setModel(model);

        setRootVisible(false);
    }
}
