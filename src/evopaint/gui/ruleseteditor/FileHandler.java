/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.ExampleRuleSetCollectionFactory;
import evopaint.pixel.rulebased.RuleSetCollection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;

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

    public synchronized void updateSetTitle(String oldTitle, String newTitle) {
    }

    public synchronized void updateSetDescription(String title, String newDescription) {
    }

    public synchronized void updateCollectionTitle(String oldTitle, String newTitle) {
    }

    public synchronized void updateCollectionDescription(String title, String newDescription) {
    }

    private synchronized void createExampleCollections(File dir) {
        RuleSetCollection ruleSetCollection = ExampleRuleSetCollectionFactory.createSimple();
        try {
            File collectionFile = new File(dir, ruleSetCollection.getName().replace(" ", "_").toLowerCase() + ".epc");
            collectionFile.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(collectionFile));
            out.writeObject(ruleSetCollection);
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public FileHandler() {
        checkFiles();
    }
}
