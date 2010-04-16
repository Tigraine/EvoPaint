/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.gui;

import evopaint.Configuration;
import evopaint.Paint;
import evopaint.commands.*;
import evopaint.Selection;
import evopaint.World;
import evopaint.Perception;
import evopaint.gui.util.WrappingScalableCanvas;
import evopaint.util.logging.Logger;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author tam
 */

public class Showcase extends WrappingScalableCanvas implements MouseInputListener, MouseWheelListener, Observer, SelectionManager {
    private Configuration configuration;

    private MainFrame mainFrame;

    private boolean leftButtonPressed = false;
    private boolean toggleMouseButton2Drag = false;

    private PaintCommand paintCommand;
    private MoveCommand moveCommand;
    private SelectCommand selectCommand;

    private SelectionList currentSelections = new SelectionList();
    private Selection activeSelection;

    private boolean isDrawingSelection = false;
    private Point selectionStartPoint;
    private Point currentMouseDragPosition;

    private BrushIndicatorOverlay brushIndicatorOverlay;
    private Timer paintingTimer;
    private Painter painter;

    public Showcase(Configuration configuration, MainFrame mf, World world, Perception perception, CommandFactory commandFactory) {
        super(perception.getImage());

        this.configuration = configuration;
        this.mainFrame = mf;
        this.paintCommand = new PaintCommand(configuration);
        this.moveCommand = new MoveCommand(configuration);
        this.moveCommand.setCanvas(this);
        this.selectCommand = commandFactory.GetSelectCommand(currentSelections);

        this.currentSelections.addObserver(this);

        this.brushIndicatorOverlay = new BrushIndicatorOverlay(this,
                new Rectangle(configuration.brush.size, configuration.brush.size));

        this.painter = new Painter();
        this.paintingTimer = new Timer(0, this.painter);
        this.paintingTimer.setDelay(10);

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public SelectionList getCurrentSelections() {
        return currentSelections;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        
        if (isDrawingSelection && selectionStartPoint != null && currentMouseDragPosition != null) {
            Point startPoint = selectionStartPoint;
            Point endPoint = currentMouseDragPosition;
            if (startPoint.x > endPoint.x || startPoint.y > endPoint.y) {
                Point temp = endPoint;
                endPoint = startPoint;
                startPoint = temp;
            }
            // FIXME Selection.draw(g2, startPoint, endPoint, scale);
        }

        for(Selection selection : currentSelections) {
            // FIXME if (selection.isHighlighted())
                // FIXME Selection.draw(g2, selection, scale);
        }
        if (activeSelection != null) {
           // FIXME Selection.draw(g2, activeSelection, scale);
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        // needs to be checked _before_ zooming
        if (e.getSource() == this && mainFrame.getActiveTool() == PaintCommand.class) {
            brushIndicatorOverlay.setBounds(new Rectangle(
                    transformToImageSpace(e.getPoint()),
                    new Dimension(configuration.brush.size, configuration.brush.size)));
        }
        
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
                painter.setLocation(e.getPoint());
                paintingTimer.start();
                configuration.paint.rememberCurrent();
            } else if (mainFrame.getActiveTool() == MoveCommand.class) {
                moveCommand.setSource(e.getPoint());
                //moveCommand.setScale(this.scale);
            } else if (mainFrame.getActiveTool() == SelectCommand.class) {
                // FIXME this.selectionStartPoint = SelectCommand.TranslatePointToScale(e.getPoint(), scale);
                this.isDrawingSelection = true;
                // FIXME selectCommand.setLocation(e.getPoint(), scale);
                selectCommand.execute();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            configuration.paint.showHistory(this, e.getPoint());
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            toggleMouseButton2Drag = true;
            moveCommand.setSource(e.getPoint());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButtonPressed = false;
            paintingTimer.stop();
            if (mainFrame.getActiveTool() == SelectCommand.class) {
                this.isDrawingSelection = false;
                // FIXME selectCommand.setLocation(e.getPoint(), scale);
                selectCommand.execute();
            }
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            toggleMouseButton2Drag = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (leftButtonPressed == true) {
            // FIXME this.currentMouseDragPosition = SelectCommand.TranslatePointToScale(e.getPoint(), scale);
            if (mainFrame.getActiveTool() == PaintCommand.class) {
                painter.setLocation(e.getPoint());
            } else if (mainFrame.getActiveTool() == MoveCommand.class) {
                moveCommand.setDestination(e.getPoint());
                moveCommand.execute();
            }
        } else if (toggleMouseButton2Drag == true) {
            moveCommand.setDestination(e.getPoint());
            moveCommand.execute();
        }
        if (mainFrame.getActiveTool() == PaintCommand.class) {
            brushIndicatorOverlay.setBounds(new Rectangle(
                    transformToImageSpace(e.getPoint()),
                    new Dimension(configuration.brush.size, configuration.brush.size)));
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if (mainFrame.getActiveTool() == PaintCommand.class) {
            subscribe(brushIndicatorOverlay);
        }
    }

    public void mouseExited(MouseEvent e) {
        unsubscribe(brushIndicatorOverlay);
    }

    public void mouseMoved(MouseEvent e) {
        if (mainFrame.getActiveTool() == PaintCommand.class) {
            brushIndicatorOverlay.setBounds(new Rectangle(
                    transformToImageSpace(e.getPoint()),
                    new Dimension(configuration.brush.size, configuration.brush.size)));
        }
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

    private class Painter implements ActionListener {
        private Point location;

        public Painter() {
        }

        public void setLocation(Point locationInUserSpace) {
            this.location = transformToImageSpace(locationInUserSpace);
        }

        public void actionPerformed(ActionEvent e) {
            paintCommand.setLocation(location);
            paintCommand.execute();
        }
    }

    /* a nice idea, but it is slow since we have to draw not only the line, but the pixels into the world
    private class ContinuousPainter implements ActionListener {
        private Point location;
        private Queue<Point> destinations;

        public ContinuousPainter() {
            this.destinations = new ArrayDeque<Point>();
        }

        public void setLocation(Point locationInUserSpace) {
            this.location = transformToImageSpace(locationInUserSpace);
            this.destinations.clear();
        }

        public void setDestination(Point destinationInUserSpace) {
         this.destinations.add(transformToImageSpace(destinationInUserSpace));
        }

        public void actionPerformed(ActionEvent e) {
            Point destination = destinations.peek();
            while (destination != null) {
                if (destination.x != location.x) {
                    double gradient = gradient(location, destination);
                    if (gradient < 0.5) {
                        if (destination.y != location.y) {
                            if (destination.y > location.y) {
                                location.y += gradient;
                            } else {
                                location.y -= gradient;
                            }
                        }
                        if (destination.x > location.x) {
                            location.x += 1;
                        } else {
                            location.x -= 1;
                        }
                    } else {
                        if (destination.x > location.x) {
                            location.x += gradient;
                        } else {
                            location.x -= gradient;
                        }
                        if (destination.y > location.y) {
                            location.y += 1;
                        } else {
                            location.y -= 1;
                        }
                    }
                    break;
                }
                else if (destination.y != location.y) {
                    if (destination.y > location.y) {
                        location.y += 1;
                    } else {
                        location.y -= 1;
                    }
                    break;
                } else {
                    destinations.remove();
                    destination = destinations.peek();
                }
            }
            paintCommand.setLocation(location);
            paintCommand.execute();
        }

        private double gradient(Point location, Point destination) {
            assert(location.x - destination.x != 0);
            return ((double)Math.abs(destination.y - location.y)) /
                    ((double)Math.abs(destination.x - location.x));
        }

    }
   */
    
}
