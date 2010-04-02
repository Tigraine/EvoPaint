/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import evopaint.pixel.rulebased.ExampleRuleSetCollectionFactory;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author tam
 */
public class FileHandler {
    private static FileHandler FILE_HANDLER; // singleton

    private File homeDir;
    private File collectionsDir;

    public static FileHandler getHandler() {
        if (FILE_HANDLER == null) {
            FILE_HANDLER = new FileHandler();
        }
        return FILE_HANDLER;
    }

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
                for (int i = 0; i < ruleSetCollection.getNumRuleSets(); i++) {
                    RuleSet ruleSet = (RuleSet)in.readObject();
                    RuleSetNode ruleSetNode = new RuleSetNode(ruleSet);
                    collectionNode.insert(ruleSetNode, i);
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

        return model;
    }

    public synchronized void renameCollection(RuleSetCollection collection, String newName) {
        if (newName == null || collection.getName().equals(newName)) {
            return;
        }

        File collectionFile = new File(collectionsDir, makeFileName(collection.getName()));
        collectionFile.renameTo(new File(collectionsDir, makeFileName(newName)));
    }

    public synchronized void writeCollection(RuleSetCollection collection) {
        File collectionFile = new File(collectionsDir, makeFileName(collection.getName()));
 
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(collectionFile));
            out.writeObject(collection);
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
        RuleSetCollection collection = ExampleRuleSetCollectionFactory.createSimple();
        try {
            File collectionFile = new File(dir, makeFileName(collection.getName()));
            collectionFile.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(collectionFile));
            out.writeObject(collection);
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

    public FileHandler() {
        checkFiles();
    }
}
