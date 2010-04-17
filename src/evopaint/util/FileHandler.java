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

package evopaint.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;
import javax.swing.JFileChooser;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
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

        for (File collectionDir : collectionDirs) {
            if (false == collectionDir.isDirectory()) {
                collectionDir.delete();
            }
            
            File metadataFile = new File(collectionDir, "metadata.xml");
            RuleSetCollection ruleSetCollection = (RuleSetCollection)importFromFile(metadataFile);
            
            if (ruleSetCollection == null) {
                continue;
            }

            CollectionNode collectionNode = new CollectionNode(ruleSetCollection);
            for (File file : collectionDir.listFiles()) {
                if (file.getName().equals("metadata.xml")) {
                    continue;
                }
                
                RuleSet ruleSet = (RuleSet)importFromFile(file);

                if (ruleSet == null) {
                    continue;
                }

                RuleSetNode ruleSetNode = new RuleSetNode(ruleSet);
                collectionNode.insert(ruleSetNode, collectionNode.getChildCount());
            }
            model.insertNodeInto(collectionNode, root, root.getChildCount());
        }
        model.addTreeModelListener(this);
        return model;
    }

    private synchronized void createExampleCollections(File dir) {
        try {
            URL examplesURL = getClass().getResource("/evopaint/examples");
            File examplesDir = new File(examplesURL.getFile());
            for (File exampleCollectionDir : examplesDir.listFiles()) {
                assert(exampleCollectionDir.isDirectory());
                File realCollectionDir = new File(collectionsDir, exampleCollectionDir.getName());
                assert (false == realCollectionDir.exists());
                realCollectionDir.mkdir();
                File [] exampleRuleSetFiles = exampleCollectionDir.listFiles();
                for (File exampleRuleSetFile : exampleRuleSetFiles) {
                    FileInputStream in = new FileInputStream(exampleRuleSetFile);
                    File realRuleSetFile = new File(realCollectionDir, exampleRuleSetFile.getName());
                    FileOutputStream out = new FileOutputStream(realRuleSetFile);
                    byte [] buffer = new byte[1024];
                    int readBytes;
                    while ((readBytes = in.read(buffer)) > 0) {
                        out.write(buffer, 0, readBytes);
                    }
                    in.close();
                    out.close();
                }
            }
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
            RuleSetNode ruleSetNode = (RuleSetNode)changedNode;
            CollectionNode collectionNode = (CollectionNode)parentNode;
            assert(collectionNode != null);
            File ruleSetFile = new File(collectionsDir,
                    makeDirectoryName(collectionNode.getName()) + File.separator +
                    makeFileName(ruleSetNode.getName()));
            exportToFile((RuleSet)ruleSetNode.getUserObject(), ruleSetFile);
            return;
        }

        if (changedNode instanceof CollectionNode) {
            //System.out.println("collection directory changed");
            CollectionNode collectionNode = (CollectionNode)changedNode;
            assert(collectionNode != null);
            File [] collectionDirs = collectionsDir.listFiles();

            for (int i = 0; i < collectionDirs.length; i++) {
                boolean found = false;
                Enumeration enumeration = parentNode.children();
                while (enumeration.hasMoreElements()) {
                    String name = makeDirectoryName(((CollectionNode)enumeration.nextElement()).getName());
                    if (collectionDirs[i].getName().equals(name)) {
                        found = true;
                        break;
                    }
                }
                if (found == false) {
                    if (false == collectionDirs[i].renameTo(new File(collectionsDir,
                            makeDirectoryName(collectionNode.getName())))) {
                        System.out.println("Failed to rename collection directory: " + collectionDirs[i].getAbsolutePath());
                    } else {
                        File metaDataFile = new File(collectionsDir,
                                makeDirectoryName(collectionNode.getName()) +
                                "/metadata.xml");

                        exportToFile(collectionNode.getUserObject(), metaDataFile);
                    }
                    break;
                }
            }
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
            File collectionDir = new File(collectionsDir,
                    makeDirectoryName(((CollectionNode)addedNode).getName()));
            if (false == collectionDir.exists()) {
                collectionDir.mkdir();
            }

            File metaDataFile = new File(collectionDir, "metadata.xml");
            exportToFile(addedNode.getUserObject(), metaDataFile);
            return;
        }

        if (addedNode instanceof RuleSetNode) {
            CollectionNode collectionNode = (CollectionNode)parentNode;
            assert(collectionNode != null);
            File ruleSetFile = new File(collectionsDir,
                makeDirectoryName(collectionNode.getName() +
                File.separator +
                makeFileName(((RuleSetNode)addedNode).getName())));
            exportToFile(addedNode.getUserObject(), ruleSetFile);
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
            String dirName = makeDirectoryName(((CollectionNode)removedNode).getName());
            File collectionDir = new File(collectionsDir, dirName);
            collectionDir.delete();
        }

        if (removedNode instanceof RuleSetNode) {
            CollectionNode collectionNode = (CollectionNode)parentNode;
            assert(collectionNode != null);
            File ruleSetFile = new File(collectionsDir,
                    makeDirectoryName(collectionNode.getName() +
                    File.separator +
                    makeFileName(((RuleSetNode)removedNode).getName())));
            if (false == ruleSetFile.delete()) {
                System.out.println("Failed to delete rule set file: " + ruleSetFile.getAbsolutePath());
            }
            return;
        }

        assert(false);
    }

    public void treeStructureChanged(TreeModelEvent e) {
        System.out.println("file handler: detected node structure change this was not expected, exiting.");
        System.exit(1);
    }

    private Object importFromFile(File file) {
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        } catch (UnsupportedEncodingException ex) {
            ExceptionHandler.handle(ex);
        } catch (FileNotFoundException ex) {
            ExceptionHandler.handle(ex);
        }
        try {
            return xStream.fromXML(reader);
        } catch (XStreamException ex) {
            ExceptionHandler.handle(ex, false, "<p>I could not parse the file \"" + file.getAbsolutePath() + "\".</p><p>This either means this file is corrupted in some way or some internals have changed and we have no proper backwards compability yet. Please have a look at the message below and try to fix your rule set if you can. They are stored in XML format, so any text editor will do. Except Notepad.exe, because it sucks and will display your rule set without line breaks</p><p>If you cannot fix it, you can always delete the file in question and recreate the rule set using the rule set editor.</p>");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ExceptionHandler.handle(ex);
            }
        }
        return null;
    }

    public void exportToFile(Object object, File file) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            writer.write(xStream.toXML(object));
        } catch (UnsupportedEncodingException ex) {
            ExceptionHandler.handle(ex);
        } catch (FileNotFoundException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                ExceptionHandler.handle(ex);
            }
        }
    }

    public FileHandler(XStream xStream) {
        this.xStream = xStream;
        checkFiles();
    }
}
