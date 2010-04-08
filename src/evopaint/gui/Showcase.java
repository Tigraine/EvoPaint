/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.gui;

import evopaint.Abstractions.TransformationCalculation;
import evopaint.Configuration;
import evopaint.Paint;
import evopaint.commands.*;
import evopaint.Selection;
import evopaint.World;
import evopaint.Perception;
import evopaint.util.logging.Logger;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author tam
 */

public class Showcase extends JComponent implements MouseInputListener, MouseWheelListener, Observer, SelectionManager {

    private Perception perception;
    private MainFrame mainFrame;

    private Configuration configuration;

    private AffineTransform affineTransform = new AffineTransform();
    private boolean leftButtonPressed = false;
    private boolean toggleMouseButton2Drag = false;
    private int zoom = 10;
    private double scale = (double)this.zoom / 10;
    private Point translation = new Point(0, 0);

    private PaintCommand paintCommand;
    private MoveCommand moveCommand;
    private SelectCommand selectCommand;

    private SelectionList currentSelections = new SelectionList();
    private Selection activeSelection;

    private boolean isDrawingSelection = false;
    private Point selectionStartPoint;
    private Point currentMouseDragPosition;
    private Point currentMouseMovePosition;
    private boolean mouseInsideShowcase = false;

    public Showcase(Configuration configuration, MainFrame mf, World world, Perception perception, CommandFactory commandFactory) {
        this.configuration = configuration;
        this.mainFrame = mf;
        this.perception = perception;
        this.configuration.affineTransform = affineTransform;
        this.paintCommand = new PaintCommand(configuration, this.scale, translation);
        this.moveCommand = new MoveCommand(configuration);
        this.moveCommand.setTranslation(this.translation);
        this.selectCommand = commandFactory.GetSelectCommand(currentSelections);

        this.currentSelections.addObserver(this);

        this.currentMouseMovePosition = new Point(0,0);

        //addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        setCursor(new Cursor(Cursor.MOVE_CURSOR));

        this.zoom = 10;
        rescale();

        setBorder(null);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    public double getScale() {
        return scale;
    }

    public SelectionList getCurrentSelections() {
        return currentSelections;
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage image = perception.getImage();

        Graphics2D g2 = (Graphics2D) g;
        g2.scale(this.scale, this.scale);
        g2.translate(translation.x, translation.y);

        // paint 9 tiles of the origininal image
        // clip it
        //g2.clip(new Rectangle(image.getWidth(), image.getHeight()));

        double w = configuration.dimension.width;
        double h = configuration.dimension.height;
        // paint NW
        g2.translate((-1) * w, (-1) * h);
        g2.drawImage(image, null, null);
        // paint N
        g2.translate(w, 0);
        g2.drawImage(image, null, null);
        // paint NE
        g2.translate(w, 0);
        g2.drawImage(image, null, null);
        // paint E
        g2.translate(0, h);
        g2.drawImage(image, null, null);
        // paint SE
        g2.translate(0, h);
        g2.drawImage(image, null, null);
        // paint S
        g2.translate((-1) * w, 0);
        g2.drawImage(image, null, null);
        // paint SW
        g2.translate((-1) * w, 0);
        g2.drawImage(image, null, null);
        // paint W
        g2.translate(0, (-1) * h);
        g2.drawImage(image, null, null);
        // back to normal
        g2.translate(w, 0);
        g2.drawImage(image, null, null);

        if (mainFrame.getActiveTool() == PaintCommand.class && mouseInsideShowcase) {
            float alpha = .5f;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.WHITE);

            Point showCaseLocation = getLocationOnScreen();
            Point pointerLocation = MouseInfo.getPointerInfo().getLocation();
            Point relativeLocation = new Point(pointerLocation.x - showCaseLocation.x, pointerLocation.y - showCaseLocation.y);
            Point worldPoint = TransformationCalculation.fromScreenToWorld(configuration, relativeLocation, scale, translation);

            Rectangle paintIndicator = new Rectangle(worldPoint.x - (int)(configuration.brush.size / 2),
                    worldPoint.y - (int)(configuration.brush.size / 2),
                    configuration.brush.size, configuration.brush.size);

            g2.fill(paintIndicator);

           // if (relativeLocation.x + paintIndicator.width > g) {
           //     g2.fillRect(0, paintIndicator.y, (int)(((relativeLocation.x + paintIndicator.width) - getWidth()) / scale), paintIndicator.height);
           // }
        }
        
        if (isDrawingSelection && selectionStartPoint != null && currentMouseDragPosition != null) {
            Point startPoint = selectionStartPoint;
            Point endPoint = currentMouseDragPosition;
            if (startPoint.x > endPoint.x || startPoint.y > endPoint.y) {
                Point temp = endPoint;
                endPoint = startPoint;
                startPoint = temp;
            }
            Selection.draw(g2, startPoint, endPoint, scale);
        }

        for(Selection selection : currentSelections) {
            if (selection.isHighlighted())
                Selection.draw(g2, selection, scale);
        }
        if (activeSelection != null) {
            Selection.draw(g2, activeSelection, scale);
        }
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
        //this.moveCommand.setScale(this.scale);
        setPreferredSize(new Dimension(
                (int) Math.ceil(perception.getImage().getWidth() * this.scale),
                (int) Math.ceil(perception.getImage().getHeight() * this.scale)));
        //((JScrollPane)getParent().getParent().getParent().getParent()).setMaximumSize(getPreferredSize());
//        ((JScrollPane)getParent().getParent().getParent().getParent()).repaint();
  //      ((JScrollPane)getParent().getParent().getParent().getParent()).revalidate();
        //((Box)getParent().getParent().getParent()).revalidate();
        revalidate();
        //mainFrame.pack();
    }



    public void mouseWheelMoved(MouseWheelEvent e) {
        ZoomCommand zoomCommand ;
        if (e.getWheelRotation() < 0) 
            zoomCommand = new ZoomInCommand(this);
        else
            zoomCommand = new ZoomOutCommand(this);
        zoomCommand.execute();
        currentMouseMovePosition = e.getPoint();
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
                this.selectionStartPoint = SelectCommand.TranslatePointToScale(e.getPoint(), scale);
                this.isDrawingSelection = true;
                selectCommand.setLocation(e.getPoint(), scale);
                selectCommand.execute();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
             if (configuration.paintHistory.size() < 1) {
                 return;
             }
            //if (false == e.isPopupTrigger()) {
            //    return;
            //}
            final JPopupMenu paintHistoryMenu = new JPopupMenu("Paint History");
            for (final Paint paint : configuration.paintHistory) {
                JMenuItem menuItem = paint.toMenuItem();
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        configuration.paint = paint;
                        mainFrame.setPaint(paint);
                        if (false == configuration.paintHistory.getFirst().equals(paint) &&
                                configuration.paintHistory.contains(paint)) {
                            configuration.paintHistory.remove(paint);
                            configuration.paintHistory.addFirst(paint);
                        }
                    }
                });
                paintHistoryMenu.add(menuItem);
            }
            paintHistoryMenu.show(e.getComponent(), e.getX(), e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            toggleMouseButton2Drag = true;
            moveCommand.setSource(e.getPoint());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButtonPressed = false;
            if (mainFrame.getActiveTool() == SelectCommand.class) {
                this.isDrawingSelection = false;
                selectCommand.setLocation(e.getPoint(), scale);
                selectCommand.execute();
            }
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            toggleMouseButton2Drag = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (leftButtonPressed == true) {
            this.currentMouseDragPosition = SelectCommand.TranslatePointToScale(e.getPoint(), scale);
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
        } else if (toggleMouseButton2Drag == true) {
            moveCommand.setDestination(e.getPoint());
            moveCommand.execute();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        mouseInsideShowcase = true;
    }

    public void mouseExited(MouseEvent e) {
        mouseInsideShowcase = false;
    }

    public void mouseMoved(MouseEvent e) {
        currentMouseMovePosition = e.getPoint();
    }

    public Selection getActiveSelection() {
        return activeSelection;
    }

    public void clearSelections() {
        this.activeSelection = null;
        this.currentSelections.clear();
    }

    public void setActiveSelection(Selection selection) {
        this.activeSelection = selection;
        ClearSelectionHighlight();
    }

    private void ClearSelectionHighlight() {
        for(Selection sel : currentSelections ){
            sel.setHighlighted(false);
        }
    }

    public void removeActiveSelection() {
        this.currentSelections.remove(activeSelection);
        activeSelection = null;
    }

    public void update(Observable o, Object arg) {
        SelectionList.SelectionListEventArgs selectionEvent = (SelectionList.SelectionListEventArgs) arg;
        if (selectionEvent.getChangeType() == SelectionList.ChangeType.ITEM_ADDED) {
            Selection selection = selectionEvent.getSelection();
            this.activeSelection = selection;
            Logger.log.error("Selection from %s-%s to %s-%s", selection.getStartPoint().getX(), selection.getStartPoint().getY(), selection.getEndPoint().getX(), selection.getEndPoint().getY());
        }
    }
}
