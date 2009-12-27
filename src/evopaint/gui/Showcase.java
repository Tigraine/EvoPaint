/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.gui;

import evopaint.EvoPaint;
import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import evopaint.commands.PauseCommand;
import evopaint.commands.ResumeCommand;
import evopaint.commands.ZoomCommand;
import evopaint.interfaces.ICommand;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author tam
 */
public class Showcase extends JPanel {

    private MainFrame mainFrame;
    private EvoPaint evopaint;
    private double scale;
    private Point viewport;
    private AffineTransform affineTransform = new AffineTransform();
    private Point lastMousePoint;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = this.evopaint.getImage();

        //Rectangle cliptangle = new Rectangle(image.getWidth(), image.getHeight());
        Graphics2D g2 = (Graphics2D) g;
        //g2.clip(cliptangle);
        //g2.
        //g2.drawRenderedImage(image, this.affineTransform);
        //g2.drawImage(image, 0-image.getWidth()+this.viewPort.x, 0, null);
        //this.affineTransform.
        g.drawImage(image, 0, 0, null);
    }

    public void setViewport(Point viewport) {
        this.viewport = viewport;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    private void mouseWheel(MouseWheelEvent e) {
        ZoomCommand zoomCommand = new ZoomCommand(this, evopaint.getObserver(), e.getWheelRotation() * -1);
        zoomCommand.execute();
    }

    private void toolAction(MouseEvent e) {
        if (mainFrame.getActiveTool() == PaintCommand.class) {
            ICommand command = new PaintCommand(this.evopaint.getWorld(), e.getPoint(), 20);
            command.execute();
        } else if (mainFrame.getActiveTool()  == MoveCommand.class) {
            this.lastMousePoint = e.getPoint();
            ICommand command = new PauseCommand(this.evopaint);
            command.execute();
        }
    }

    public Showcase(MainFrame mf, EvoPaint evo) {
        super();
        this.mainFrame = mf;
        this.evopaint = evo;

        setPreferredSize(new Dimension(this.evopaint.getImage().getWidth(),
                this.evopaint.getImage().getHeight()));

        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                mouseWheel(e);
            }
        });

        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    toolAction(e);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    mainFrame.showToolMenu(e.getPoint());
                }

            }

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (mainFrame.getActiveTool() == MoveCommand.class) {
                        lastMousePoint = e.getPoint();
                        ICommand command = new ResumeCommand(evopaint);
                        command.execute();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    mainFrame.hideToolMenu();
                }
        }

        });

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if (mainFrame.getActiveTool() == MoveCommand.class) {
                    System.out.println("draggin away");
                    //ICommand command = new MoveCommand(observer, lastMousePosition, e.getPoint());
                    //command.execute();
                    //Point src =
                    //      Point dst = e.getPoint();
                    //int dx = dst.x - src.x;
                    //int dy = dst.y - src.y;
                    //translateViewport(e.getPoint());
                }


            }
        });


    }
}
