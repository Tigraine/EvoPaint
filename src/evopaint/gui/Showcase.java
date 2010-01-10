/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.gui;

import evopaint.EvoPaint;
import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import evopaint.commands.ZoomCommand;
import evopaint.interfaces.ICommand;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
public class Showcase extends JPanel implements MouseInputListener, MouseWheelListener {

    private MainFrame mainFrame;
    private EvoPaint evopaint;
    private AffineTransform affineTransform = new AffineTransform();
    private Point draggedPoint;
    private boolean leftButtonPressed = false;
    private int zoom = 10;
    private double scale = (double)this.zoom / 10;

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage image = this.evopaint.getImage();

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
        setPreferredSize(new Dimension(
                (int) (evopaint.getImage().getWidth() * this.scale),
                (int) (evopaint.getImage().getHeight() * this.scale)));
        mainFrame.pack();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        ZoomCommand zoomCommand = new ZoomCommand(this,
                (e.getWheelRotation() < 0 ? true : false));
        zoomCommand.execute();
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButtonPressed = true;
            if (mainFrame.getActiveTool() == PaintCommand.class) {
                ICommand command = new PaintCommand(this.evopaint.getWorld(),
                        e.getPoint(), this.scale, affineTransform, 10);
                command.execute();
            } else if (mainFrame.getActiveTool() == MoveCommand.class) {
                this.draggedPoint = e.getPoint();
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
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            mainFrame.hideToolMenu();
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (leftButtonPressed == true) {
            if (mainFrame.getActiveTool() == PaintCommand.class) {
                // TODO: paint pixels between mouse drags
                // TODO: Maybe refactor state into PaintCommand
                ICommand command = new PaintCommand(this.evopaint.getWorld(),
                        e.getPoint(), this.scale, affineTransform, 10);
                command.execute();
            } else if (mainFrame.getActiveTool() == MoveCommand.class) {
                ICommand command = new MoveCommand(draggedPoint, e.getPoint(),
                        this.scale, affineTransform, evopaint.getImage());
                command.execute();

                draggedPoint = e.getPoint();
                repaint();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public Showcase(MainFrame mf, EvoPaint evo) {
        super();
        this.mainFrame = mf;
        this.evopaint = evo;

        setPreferredSize(new Dimension(this.evopaint.getImage().getWidth(),
                this.evopaint.getImage().getHeight()));

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
}
