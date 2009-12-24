/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.interfaces.IAttribute;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author tam
 */
public class SpacialAttribute implements IAttribute {
    private Point origin;
    private Dimension dimension;

    @Override
    public String toString() {
        return "(" + this.origin.x + "+" + this.dimension.width +
                "/" + this.origin.y + "+" + this.dimension.height + ")";
    }

    public Point getOrigin() {
        return origin;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public SpacialAttribute(Point origin, Dimension dimension) {
        this.origin = origin;
        this.dimension = dimension;
    }
}
