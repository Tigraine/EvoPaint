/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.Configuration;
import evopaint.EvoPaint;
import evopaint.Manifest;
import evopaint.Perception;
import evopaint.Selection;
import evopaint.World;
import evopaint.commands.CommandFactory;
import evopaint.commands.FillSelectionCommand;
import evopaint.gui.MainFrame;
import evopaint.gui.listeners.SelectionListenerFactory;
import evopaint.gui.listeners.SelectionSetNameListener;
import evopaint.pixel.Pixel;
import evopaint.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
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
    private EvoPaint evopaint;
    private Showcase showcase;
    private JMenu selectionMenu;
    private JMenu activeSelections;
    private newWizard nw;
    private MenuBar mb;

    public MenuBar(final EvoPaint evopaint, SelectionListenerFactory listenerFactory, Showcase showcase) {
        this.evopaint = evopaint;
        this.showcase = showcase;
        this.mb=this;
        // World Menu
        JMenu worldMenu = new JMenu();
        worldMenu.setText("World");
        add(worldMenu);

        // File Menu Items        
        JMenuItem newItem = new JMenuItem();
        newItem.setText("New");
        newItem.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				nw = new newWizard(mb);
			}
		});
        
        
        worldMenu.add(newItem);
        
        worldMenu.add(new JMenuItem("Open..."));
        worldMenu.add(new JMenuItem("Save"));
        worldMenu.add(new JMenuItem("Save as..."));
        worldMenu.add(new JMenuItem("Import..."));
             
        JMenuItem exportItem = new JMenuItem();
        exportItem.setText("Export");
        exportItem.addActionListener(new ExportDialog(evopaint));
        
        worldMenu.add(exportItem);
        
        worldMenu.add(new JMenuItem("Options..."));
        JMenuItem endItem = new JMenuItem();
        endItem.setText("End");
        endItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(getRootPane(), "Do you really want to Exit?", "Exit EvoPaint", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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
        JMenuItem fillSelection = new JMenuItem("Fill");
        fillSelection.addActionListener(new FillSelectionCommand(showcase));
        selectionMenu.add(fillSelection);
        selectionMenu.add(new JMenuItem("Open as new"));
        selectionMenu.add(new JMenuItem("Copy"));
        selectionMenu.add(new JMenuItem("Options..."));
        activeSelections = new JMenu("Selections");
        selectionMenu.add(activeSelections);
        JMenuItem clearSelections = new JMenuItem("Clear Selections");
        clearSelections.addActionListener(listenerFactory.CreateClearSelectionsListener());
        selectionMenu.add(clearSelections);

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
                JOptionPane.showMessageDialog(getRootPane(), msg, "About EvoPaint", JOptionPane.INFORMATION_MESSAGE);
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
            addMouseListener(new SelectionMouseListener());
        }

        public void update(Observable o, Object arg) {
            Selection selection = (Selection) o;
            UpdateName(selection);
        }

        private void UpdateName(Selection selection) {
            this.setText(selection.getSelectionName());
        }

        private class SelectionMouseListener implements MouseListener
        {
            public void mouseClicked(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void mousePressed(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void mouseReleased(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void mouseEntered(MouseEvent e) {
                selection.setHighlighted(true);
            }

            public void mouseExited(MouseEvent e) {
                selection.setHighlighted(false);
            }
        }
    }

	public void newEvolution(int x, int y) {
		evopaint.getConfiguration().running = false;
		//todo wizard code & implementation of a new evolution
		
		Configuration newConf = new Configuration();
                newConf.dimension = new Dimension(x,y);
		evopaint.setConfiguration(newConf);
		
		
		evopaint.setWorld(new World(new Pixel[newConf.dimension.width * newConf.dimension.height],0, newConf));
		evopaint.setPerception(new Perception(new BufferedImage
                (newConf.dimension.width, newConf.dimension.height,
                BufferedImage.TYPE_INT_RGB)));

        evopaint.getPerception().createImage(evopaint.getWorld());
       
        //check evopaint
		//check config
		//check work
		// implement
		// new config 
		// evopaint= config
		// evopaint work
        
		evopaint.getFrame().setConfiguration(newConf);
		evopaint.getFrame().removeGraf();
		evopaint.getFrame().initializeCommands(evopaint);
		evopaint.getFrame().initSecond(evopaint);
		

		evopaint.getConfiguration().running = true;
		
	}
}
