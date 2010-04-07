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
import evopaint.util.logging.Logger;
import java.awt.Cursor;
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

public class Showcase extends JComponent implements MouseInputListener, MouseWheelListener, Observer, SelectionManager, Scrollable {

    private Perception perception;
    private MainFrame mainFrame;

    private Configuration configuration;

    private AffineTransform affineTransform = new AffineTransform();
    private boolean leftButtonPressed = false;
    private boolean toggleMouseButton2Drag = false;
    private int zoom = 10;
    private double scale = (double)this.zoom / 10;


    private PaintCommand paintCommand;
    private MoveCommand moveCommand;
    private SelectCommand selectCommand;

    private SelectionList currentSelections = new SelectionList();
    private Selection activeSelection;

    private boolean isDrawingSelection = false;
    private Point selectionStartPoint;
    private Point currentMouseDragPosition;

    public Showcase(Configuration configuration, MainFrame mf, World world, Perception perception, CommandFactory commandFactory) {
        this.configuration = configuration;
        this.mainFrame = mf;
        this.perception = perception;
        this.configuration.affineTransform = affineTransform;
        this.paintCommand = new PaintCommand(configuration, this.scale, affineTransform);
        this.moveCommand = new MoveCommand(configuration);
        this.selectCommand = commandFactory.GetSelectCommand(currentSelections);

        this.currentSelections.addObserver(this);

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
        this.moveCommand.setScale(this.scale);
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
            //if (false == e.isPopupTrigger()) {
            //    return;
            //}
            final JPopupMenu paintHistoryMenu = new JPopupMenu("Paint History");
            for (final Paint paint : configuration.paintHistory) {
                JMenuItem menuItem = new JMenuItem("<html>" + paint.toHTML() + "</html>");
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        configuration.paint = paint;
                        mainFrame.setPaint(paint);
                        if (configuration.paintHistory.contains(paint)) {
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
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
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

    /**
     * Components that display logical rows or columns shouls compute the scroll increment that
     * will completely expos one block of rows or columns depending upon the orientation.
     *
     * Graphics components should compute how much of the image one wants to scroll each time
     * the view port is moved by a Unit Increment. In this case, it is a percentage (10%) of
     * the overall image.
     *
     * Scrolling containers like JScrollPane will use this methode each time the user requests
     * a block scroll.
     *
     * This value comes into play when one clicks the scroll bar in the area above or below
     * the slider. The JScrollPane then moves the view to the next visible area.
     *
     * @param &lt;b&gt;visibleRect&lt;/b&gt; - The view area visible within the viewport.
     * @param &lt;b&gt;orientation&lt;/b&gt; - Either &lt;b&gt;SwingConstants.VERTICAL&lt;/b&gt; or &lt;b&gt;SwingConstants.HORIZONTAL&lt;/b&gt;
     * @param &lt;b&gt;direction&lt;/b&gt; - If less than zero scroll up/left. If greater than zero scroll down/right.
     * @return The "Block" increment for scrolling in a specific direction.
     *
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (orientation == SwingConstants.VERTICAL) ? visibleRect.height / 10 : visibleRect.width / 10;
    }

    /**
     * Components that display logical rows or columns should compute the scroll increment that
     * will completely expos one block of rows or columns depending upon the orientation.
     *
     * Graphics components should compute how much of the image one wants to scroll each time
     * the view port is moved by a Unit Increment. In this case, it is a percentage (10%) of
     * the overall image.
     *
     * Scrolling containers like JScrollPane will use this method each time the user requests
     * a unit scroll.
     *
     * @param &lt;b&gt;visibleRect&lt;/b&gt; - The view area visible within the viewport.
     * @param &lt;b&gt;orientation&lt;/b&gt; - Either &lt;b&gt;SwingConstants.VERTICAL&lt;/b&gt; or &lt;b&gt;SwingConstants.HORIZONTAL&lt;/b&gt;
     * @param &lt;b&gt;direction&lt;/b&gt; - If less than zero scroll up/left. If greater than zero scroll down/right.
     * @return The "Block" increment for scrolling in a specific direction.
     *
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {

        return (orientation == SwingConstants.VERTICAL) ? visibleRect.height / 10 : visibleRect.width / 10;
    }

    /**
     * Returns the preferred size of the viewport for a view component. This is the size of it requires
     * to display the entire size of the component. A component without any properties that would
     * effect the viewport size should return getPreferredSize() here.
     */
    public Dimension getPreferredScrollableViewportSize() {
        return this.getPreferredSize();
    }

    /**
     * Returns true if a viewport should always force the width of this Scrollable to
     * match the width of the viewport. For example a normal text view that supported
     * line wrapping would return a true here, since it would be undesirable for wrapped
     * lines to dissappear beyond the right edge of the viewport. Not that returning
     * a true for a Scrollable whose ancestor is a JScrollPane effectively disables
     * horizontal scrolling!!!!
     *
     */
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    /**
     * Returns true if a viewport should always force the height of this Scrollable to
     * match the width of the viewport. For example a normal text view that supported
     * columnar text that flowed text in left to right columns would return a true here,
     * to effectively disable vertical scrolling.
     *
     * Returing a true for a Scrollable whose ancestor is a JScrollPane effectively disables
     * vertical scrolling!!!!
     */
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

}
