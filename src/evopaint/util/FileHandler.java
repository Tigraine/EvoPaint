/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import evopaint.pixel.rulebased.ExampleRuleSetCollectionFactory;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author tam
 */
public class FileHandler implements TreeModelListener {

    private File homeDir;
    private File collectionsDir;

    public File getHomeDir() {
        return homeDir;
    }

    public File getCollectionsDir() {
        return collectionsDir;
    }
    
    public synchronized void checkFiles() {
        File userDir = new JFileChooser().getFileSystemView().getDefaultDirectory();
        homeDir = new File(userDir, ".evopaint");
        if (homeDir.exists() == false) {
            homeDir.mkdir();
        }
        collectionsDir = new File(homeDir, "collections");
        if (collectionsDir.exists() == false) {
            collectionsDir.mkdir();
            createExampleCollections(collectionsDir);
        }
    }

    public synchronized DefaultTreeModel readCollections() {
        File [] collectionFiles = collectionsDir.listFiles();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultTreeModel model = new DefaultTreeModel(root, true);

        try {
            for (File collectionFile : collectionFiles) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(collectionFile));
                RuleSetCollection ruleSetCollection = (RuleSetCollection)in.readObject();
                CollectionNode collectionNode = new CollectionNode(ruleSetCollection);
                try {
                    while (true) {
                        RuleSet ruleSet = (RuleSet)in.readObject();
                        RuleSetNode ruleSetNode = new RuleSetNode(ruleSet);
                        collectionNode.insert(ruleSetNode, collectionNode.getChildCount());
                    }
                } catch (EOFException ex) {
                    // lulz, EOF
                }
                in.close();
                model.insertNodeInto(collectionNode, root, root.getChildCount());
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

        model.addTreeModelListener(this);
        return model;
    }

    public synchronized void renameCollection(RuleSetCollection collection, String newName) {
        if (newName == null || collection.getName().equals(newName)) {
            return;
        }

        File collectionFile = new File(collectionsDir, makeFileName(collection.getName()));
        collectionFile.renameTo(new File(collectionsDir, makeFileName(newName)));
    }

    public synchronized void writeCollection(CollectionNode collectionNode) {
        RuleSetCollection collection = (RuleSetCollection)collectionNode.getUserObject();
        File collectionFile = new File(collectionsDir, makeFileName(collection.getName()));

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(collectionFile));
            out.writeObject(collection);
            Enumeration enumeration = collectionNode.children();
            if (enumeration != null) {
                while (enumeration.hasMoreElements()) {
                    RuleSetNode ruleSetNode = (RuleSetNode)enumeration.nextElement();
                    RuleSet ruleSet = (RuleSet)ruleSetNode.getUserObject();
                    out.writeObject(ruleSet);
                }
            }
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public synchronized void deleteCollection(RuleSetCollection collection) {
        File collectionFile = new File(collectionsDir, makeFileName(collection.getName()));
        collectionFile.delete();
    }

    private synchronized void createExampleCollections(File dir) {
        RuleSetCollection collection = ExampleRuleSetCollectionFactory.createCollectionSimple();
        List<RuleSet> ruleSets = ExampleRuleSetCollectionFactory.createRuleSetsSimple();
        try {
            File collectionFile = new File(dir, makeFileName(collection.getName()));
            collectionFile.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(collectionFile));
            out.writeObject(collection);
            for (RuleSet ruleSet : ruleSets) {
                out.writeObject(ruleSet);
            }
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private String makeFileName(String title) {
        return title.replace(" ", "_").toLowerCase() + ".epc";
    }

    // called whenever we reset the name of a collection or rule set
    public void treeNodesChanged(TreeModelEvent e) {
        System.out.println("file handler: detected node modification");
    }

    public void treeNodesInserted(TreeModelEvent e) {
        System.out.println("file handler: detected node insertion");
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
        System.out.println("file handler: detected node deletion");
        
        /*
        Object o = parentNode.getUserObject();
        DefaultMutableTreeNode [] children =
                (DefaultMutableTreeNode [])e.getChildren();

        // if parent of deleted node contains a collection, delete the collection
        if (o instanceof RuleSetCollection) {
            RuleSetCollection collection = (RuleSetCollection)o;
            deleteCollection(collection);
            return;
        }

        // if deleted node contains a rule set, update the collection
        if (o instanceof RuleSet) {
            CollectionNode collectionNode = (CollectionNode)node.getParent();
            assert(collectionNode != null);
            RuleSetCollection collection =
                    (RuleSetCollection)collectionNode.getUserObject();
            writeCollection(collection);
            return;
        }

        assert(false);*/
    }

    public void treeStructureChanged(TreeModelEvent e) {
        System.out.println("file handler: detected node structure change");
    }

    public FileHandler() {
        checkFiles();
    }
}
