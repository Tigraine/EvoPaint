/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.gui.MainFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author tam
 */
public class MenuBar extends JMenuBar {
    public MenuBar() {

        // File Menu
        JMenu fileMenu = new JMenu();
        fileMenu.setText("File");
        add(fileMenu);

        // File Menu Items
        JMenuItem newEvoItem = new JMenuItem();
        newEvoItem.setText("New Evo");
        fileMenu.add(newEvoItem);

        JMenuItem saveItem = new JMenuItem();
        saveItem.setText("Save");
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem();
        saveAsItem.setText("Save as");
        fileMenu.add(saveAsItem);

        JMenuItem loadItem = new JMenuItem();
        loadItem.setText("Load");
        fileMenu.add(loadItem);

        JMenuItem exportItem = new JMenuItem();
        exportItem.setText("Export");
        fileMenu.add(exportItem);

        JMenuItem exitItem = new JMenuItem();
        exitItem.setText("Exit");
        fileMenu.add(exitItem);

        // Edit menu
        JMenu editMenu = new JMenu();
        editMenu.setText("Edit");
        add(editMenu);

        // help menu
        JMenu helpMenu = new JMenu();
        helpMenu.setText("?");
        add(helpMenu);
    }
}
