package evopaint.entities;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07.03.2010
 * Time: 12:41:14
 * To change this template use File | Settings | File Templates.
 */
public class Selection {
    private Point startPoint;
    private Point endPoint;

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
}
