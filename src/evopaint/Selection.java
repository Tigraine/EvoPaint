package evopaint;

import java.awt.*;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07.03.2010
 * Time: 12:41:14
 * To change this template use File | Settings | File Templates.
 */
public class Selection extends Observable {
    private Point startPoint;
    private Point endPoint;

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    private boolean highlighted;

    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
        setChanged();
        notifyObservers();
    }

    private String selectionName;

    public Selection(Point startPoint, Point endPoint)
    {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public static void draw(Graphics2D gfx, Selection selection, double scale){
        draw(gfx, selection.getStartPoint(), selection.getEndPoint(), scale);
    }
    public static void draw(Graphics2D gfx, Point startPoint, Point endPoint, double scale){
        gfx.drawRect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        //gfx.drawRect((int)(startPoint.x / scale), (int)(startPoint.y / scale), (int)((endPoint.x - startPoint.x) / scale), (int)((endPoint.y - startPoint.y) / scale));
    }

    public boolean isHighlighted() {
        return highlighted;
    }
}
