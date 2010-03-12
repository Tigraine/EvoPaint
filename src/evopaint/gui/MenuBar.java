/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.Config;
import evopaint.EvoPaint;
import evopaint.Manifest;
import evopaint.gui.MainFrame;
import evopaint.util.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author tam
 */
public class MenuBar extends JMenuBar {
    private final MainFrame mainFrame;
    private EvoPaint evopaint;

    public MenuBar(MainFrame parentFrame, final EvoPaint evopaint) {
        this.mainFrame = parentFrame;
        this.evopaint = evopaint;

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

        fileMenu.add(loadItem);

        JMenuItem exportItem = new JMenuItem();
        exportItem.setText("Export");
        fileMenu.add(exportItem);
        exportItem.addActionListener(new ExportDialog(parentFrame, evopaint));

        JMenuItem exitItem = new JMenuItem();
        exitItem.setText("Exit");
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(mainFrame, "Do you really want to Exit?", "Exit EvoPaint", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        fileMenu.add(exitItem);

        // Edit menu
        JMenu editMenu = new JMenu();
        editMenu.setText("Edit");
        add(editMenu);

        // help menu
        JMenu helpMenu = new JMenu();
        helpMenu.setText("?");
        add(helpMenu);

        JMenuItem userGuide = new JMenuItem();
        userGuide.setText("User Guide");
        userGuide.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //.getDesktop().browse(new URI("http://www.your.url"));
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(Manifest.USER_GUIDE_URL));
                } catch (IOException e1) {
                    Logger.log.error("Exception occurred during opening of Users guide: \n%s", e1);
                } catch (URISyntaxException e1) {
                    Logger.log.error("Exception occurred during opening of Users guide: \n%s", e1);
                }
            }
        });
        helpMenu.add(userGuide);

        JMenuItem getCode = new JMenuItem();
        getCode.setText("Get the Code");
        getCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(Manifest.CODE_DOWNLOAD_URL));
                } catch (URISyntaxException e1) {
                    Logger.log.error("Exception occurred during opening of Get The Code : \n%s", e1);
                } catch (IOException e1) {
                    Logger.log.error("Exception occurred during opening of Get The Code : \n%s", e1);
                }
            }
        });
        helpMenu.add(getCode);

        JMenuItem about = new JMenuItem();
        about.setText("About");
        helpMenu.add(about);
    }
}
