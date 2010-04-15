/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import com.thoughtworks.xstream.XStream;
import evopaint.pixel.rulebased.ExampleRuleSetCollectionFactory;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
    private XStream xStream;

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
        File [] collectionDirs = collectionsDir.listFiles();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultTreeModel model = new DefaultTreeModel(root, true);

        try {
            for (File collectionDir : collectionDirs) {
                if (false == collectionDir.isDirectory()) {
                    collectionDir.delete();
                }
                File metadataFile = new File(collectionDir, "metadata.xml");
                Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(metadataFile), "UTF8"));
                RuleSetCollection ruleSetCollection = (RuleSetCollection)xStream.fromXML(reader);
                reader.close();
                CollectionNode collectionNode = new CollectionNode(ruleSetCollection);
                for (File file : collectionDir.listFiles()) {
                    if (file.getName().equals("metadata.xml")) {
                        continue;
                    }
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
                    RuleSet ruleSet = (RuleSet)xStream.fromXML(reader);
                    reader.close();
                    RuleSetNode ruleSetNode = new RuleSetNode(ruleSet);
                    collectionNode.insert(ruleSetNode, collectionNode.getChildCount());
                }
                model.insertNodeInto(collectionNode, root, root.getChildCount());
            }
        } catch (FileNotFoundException ex) {
            ExceptionHandler.handle(ex);
        } catch (InvalidClassException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
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
        try {
            RuleSetCollection collection = (RuleSetCollection)collectionNode.getUserObject();

            File collectionDir = new File(collectionsDir, makeDirectoryName(collection.getName()));
            if (false == collectionDir.exists()) {
                collectionDir.mkdir();
            }

            File metaDataFile = new File(collectionDir, "metadata.xml");

            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(metaDataFile), "UTF8"));
            String xml = xStream.toXML(collection);

            writer.write(xml);
            writer.close();

            Enumeration enumerationA = collectionNode.children();
            if (enumerationA != null) {
                while (enumerationA.hasMoreElements()) {
                    RuleSetNode ruleSetNode = (RuleSetNode)enumerationA.nextElement();
                    RuleSet ruleSet = (RuleSet)ruleSetNode.getUserObject();

                    File ruleSetFile = new File(collectionDir, makeFileName(ruleSet.getName()));
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruleSetFile), "UTF8"));
                    writer.write(xStream.toXML(ruleSet));
                    writer.close();
                }
            }
        } catch (UnsupportedEncodingException ex) {
            ExceptionHandler.handle(ex);
        } catch (FileNotFoundException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
    }

    public synchronized void deleteCollection(CollectionNode collectionNode) {
        RuleSetCollection collection = (RuleSetCollection)collectionNode.getUserObject();
        File collectionFile = new File(collectionsDir, makeFileName(collection.getName()));
        //System.out.println("deleting " + collectionFile);
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
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
    }

    private String makeDirectoryName(String title) {
        return title.replace(" ", "_").toLowerCase();
    }

    private String makeFileName(String title) {
        return title.replace(" ", "_").toLowerCase() + ".epr";
    }

    public void treeNodesChanged(TreeModelEvent e) {
        DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
        DefaultMutableTreeNode changedNode = (DefaultMutableTreeNode)
                parentNode.getChildAt(e.getChildIndices()[0]);

        if (changedNode instanceof RuleSetNode) {
            CollectionNode collectionNode = (CollectionNode)parentNode;
            assert(collectionNode != null);
            writeCollection(collectionNode);
            return;
        }

        assert (false);
    }

    public void treeNodesInserted(TreeModelEvent e) {
        DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
        DefaultMutableTreeNode addedNode =
                (DefaultMutableTreeNode)e.getChildren()[0];
        //System.out.println("file handler: detected node insertion");
        
        if (addedNode instanceof CollectionNode) {
            writeCollection((CollectionNode)addedNode);
            return;
        }

        if (addedNode instanceof RuleSetNode) {
            CollectionNode collectionNode = (CollectionNode)parentNode;
            assert(collectionNode != null);
            writeCollection(collectionNode);
            return;
        }

        assert(false);
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
        DefaultMutableTreeNode removedNode =
                (DefaultMutableTreeNode)e.getChildren()[0];
        //System.out.println("file handler: detected node deletion");
        
        if (removedNode instanceof CollectionNode) {
            deleteCollection((CollectionNode)removedNode);
            return;
        }

        if (removedNode instanceof RuleSetNode) {
            CollectionNode collectionNode = (CollectionNode)parentNode;
            assert(collectionNode != null);
            writeCollection(collectionNode);
            return;
        }

        assert(false);
    }

    public void treeStructureChanged(TreeModelEvent e) {
        System.out.println("file handler: detected node structure change this was not expected, exiting.");
        System.exit(1);
    }

    public FileHandler() {
        this.xStream = new XStream();
        checkFiles();
    }
}
