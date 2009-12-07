/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.interfaces.IAttribute;
import java.awt.Point;

/**
 *
 * @author tam
 */
public class SpacialAttribute implements IAttribute {
    private Point location;

    @Override
    public String toString() {
        return "(" + this.location.x + "/" + this.location.y + ")";
    }

    public Point getLocation() {
        return location;
    }

    public SpacialAttribute(Point location) {
        this.location = location;
    }
}
