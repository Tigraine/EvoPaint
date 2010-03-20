/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.EvoPaint;
import evopaint.Manifest;
import evopaint.Selection;
import evopaint.gui.MainFrame;
import evopaint.gui.listeners.SelectionListenerFactory;
import evopaint.gui.listeners.SelectionSetNameListener;
import evopaint.util.logging.Logger;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author tam
 */
public class MenuBar extends JMenuBar implements SelectionObserver {
    private final MainFrame mainFrame;
    private EvoPaint evopaint;
    private JMenu selectionMenu;
    private JMenu activeSelections;

    public MenuBar(MainFrame parentFrame, final EvoPaint evopaint, SelectionListenerFactory listenerFactory) {
        this.mainFrame = parentFrame;
        this.evopaint = evopaint;

        // World Menu
        JMenu worldMenu = new JMenu();
        worldMenu.setText("World");
        add(worldMenu);

        // File Menu Items
        worldMenu.add(new JMenuItem("New..."));
        worldMenu.add(new JMenuItem("Open..."));
        worldMenu.add(new JMenuItem("Save"));
        worldMenu.add(new JMenuItem("Save as..."));
        worldMenu.add(new JMenuItem("Import..."));
        worldMenu.add(new JMenuItem("Export..."));
        worldMenu.add(new JMenuItem("Options..."));
        JMenuItem endItem = new JMenuItem();
        endItem.setText("End");
        endItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(mainFrame, "Do you really want to Exit?", "Exit EvoPaint", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        worldMenu.add(endItem);

        // selection menu
        selectionMenu = new JMenu("Selection");
        add(selectionMenu);

        JMenuItem selectionSetName = new JMenuItem("Set Name...");
        selectionMenu.add(selectionSetName);
        selectionSetName.addActionListener(listenerFactory.CreateSelectionSetNameListener());
        selectionMenu.add(new JMenuItem("Invert"));
        selectionMenu.add(new JMenuItem("Open as new"));
        selectionMenu.add(new JMenuItem("Copy"));
        selectionMenu.add(new JMenuItem("Options..."));
        activeSelections = new JMenu("Selections");
        selectionMenu.add(activeSelections);

        // info menu
        JMenu infoMenu = new JMenu();
        infoMenu.setText("Info");
        add(infoMenu);

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
        infoMenu.add(userGuide);

        JMenuItem getCode = new JMenuItem();
        getCode.setText("Get the source code");
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
        infoMenu.add(getCode);

        JMenuItem about = new JMenuItem();
        about.setText("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = "EvoPaint is being developed as part of a software lab\n" +
                                "for the Bachelor's Degree at the\n" +
                                "University of Klagenfurt, Austria.\n" +
                                "\n" +
                                "Enjoy.";
                JOptionPane.showMessageDialog(mainFrame, msg, "About EvoPaint", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        infoMenu.add(about);

    }

    public void addSelection(Selection selection) {
        activeSelections.add(new SelectionWrapper(selection));
    }

    private class SelectionWrapper extends JMenuItem implements Observer
    {
        private Selection selection;

        private SelectionWrapper(Selection selection) {
            selection.addObserver(this);
            this.selection = selection;
            UpdateName(selection);
        }

        public void update(Observable o, Object arg) {
            Selection selection = (Selection) o;
            UpdateName(selection);
        }

        private void UpdateName(Selection selection) {
            this.setText(selection.getSelectionName());
        }
    }
}
