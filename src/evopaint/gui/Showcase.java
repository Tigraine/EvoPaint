/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.gui;

import evopaint.commands.*;
import evopaint.Selection;
import evopaint.World;
import evopaint.Perception;
import evopaint.util.logging.Logger;
import java.awt.Cursor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author tam
 */
public class Showcase extends JPanel implements MouseInputListener, MouseWheelListener, SelectionReceiver {

    private Perception perception;
    private MainFrame mainFrame;
    private World world;
    private AffineTransform affineTransform = new AffineTransform();
    private boolean leftButtonPressed = false;
    private int zoom = 10;
    private double scale = (double)this.zoom / 10;
    private PaintCommand paintCommand;
    private MoveCommand moveCommand;
    private SelectCommand selectCommand;
    private Selection currentSelection;

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage image = perception.getImage();
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(this.scale, this.scale);

        // paint 9 tiles of the origininal image
        // clip it
        g2.clip(new Rectangle(image.getWidth(), image.getHeight()));

        double w = image.getWidth();
        double h = image.getHeight();
        // paint NW
        affineTransform.translate((-1) * w, (-1) * h);
        g2.drawRenderedImage(image, this.affineTransform);
        // paint N
        affineTransform.translate(w, 0);
        g2.drawRenderedImage(image, this.affineTransform);
        // paint NE
        affineTransform.translate(w, 0);
        g2.drawRenderedImage(image, this.affineTransform);
        // paint E
        affineTransform.translate(0, h);
        g2.drawRenderedImage(image, this.affineTransform);
        // paint SE
        affineTransform.translate(0, h);
        g2.drawRenderedImage(image, this.affineTransform);
        // paint S
        affineTransform.translate((-1) * w, 0);
        g2.drawRenderedImage(image, this.affineTransform);
        // paint SW
        affineTransform.translate((-1) * w, 0);
        g2.drawRenderedImage(image, this.affineTransform);
        // paint W
        affineTransform.translate(0, (-1) * h);
        g2.drawRenderedImage(image, this.affineTransform);
        // back to normal
        affineTransform.translate(w, 0);
        g2.drawRenderedImage(image, this.affineTransform);
    }

    public void zoomIn() {
        this.zoom += 1;
        this.rescale();
    }

    public void zoomOut() {
        if (zoom <= 1) {
            return;
        }
        
        this.zoom -= 1;
        this.rescale();
    }

    private void rescale() {
        this.scale = (double)this.zoom / 10;
        this.paintCommand.setScale(this.scale);
        this.moveCommand.setScale(this.scale);
        setPreferredSize(new Dimension(
                (int) (world.getWidth() * this.scale),
                (int) (world.getHeight() * this.scale)));
        mainFrame.pack();
        
    }



    public void mouseWheelMoved(MouseWheelEvent e) {
        ZoomCommand zoomCommand ;
        if (e.getWheelRotation() < 0)
            zoomCommand = new ZoomInCommand(this);
        else
            zoomCommand = new ZoomOutCommand(this);
        zoomCommand.execute();
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButtonPressed = true;
            if (mainFrame.getActiveTool() == PaintCommand.class) {
                paintCommand.setLocation(e.getPoint());
                paintCommand.execute();
            } else if (mainFrame.getActiveTool() == MoveCommand.class) {
                moveCommand.setSource(e.getPoint());
                //moveCommand.setScale(this.scale);
            } else if (mainFrame.getActiveTool() == SelectCommand.class) {
                selectCommand.setLocation(e.getPoint());
                selectCommand.execute();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (leftButtonPressed == false) {
                mainFrame.showToolMenu(e.getPoint());
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButtonPressed = false;
            if (mainFrame.getActiveTool() == SelectCommand.class) {
                selectCommand.setLocation(e.getPoint());
                selectCommand.execute();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            mainFrame.hideToolMenu();
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (leftButtonPressed == true) {
            if (mainFrame.getActiveTool() == PaintCommand.class) {
                // TODO: paint pixels between mouse drags
                // TODO: Maybe refactor state into PaintCommand
                paintCommand.setLocation(e.getPoint());
                paintCommand.execute();
            } else if (mainFrame.getActiveTool() == MoveCommand.class) {
                moveCommand.setDestination(e.getPoint());
                moveCommand.execute();
                //repaint();
                //paintImmediately(0, 0, getWidth(), getHeight());
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if (mainFrame.getActiveTool() == MoveCommand.class) {
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else if (mainFrame.getActiveTool() == PaintCommand.class) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public Showcase(MainFrame mf, World world, Perception perception) {
        super();

        this.mainFrame = mf;
        this.world = world;
        this.perception = perception;
        this.paintCommand = new PaintCommand(this.world, this.mainFrame,this.scale, affineTransform, 50 ,0x000000FF);

        this.moveCommand = new MoveCommand(affineTransform, world.getWidth(), world.getHeight());

        this.selectCommand = new SelectCommand(this);

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        this.zoom = 10;
        this.rescale();
    }

    public void setSelection(Selection selection) {
        this.currentSelection = selection;
        Logger.log.error("Selection from %s-%s to %s-%s", selection.getStartPoint().getX(), selection.getStartPoint().getY(), selection.getEndPoint().getX(), selection.getEndPoint().getY());
    }
    
    public void setpaintCommand(){
    	this.paintCommand = new PaintCommand(this.world, this.mainFrame ,this.scale, affineTransform, mainFrame.getPop().getBrushsize(),  mainFrame.getPop().getColor());
    }
}
