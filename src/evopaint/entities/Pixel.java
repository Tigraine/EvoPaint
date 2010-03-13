/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.entities;

import evopaint.interfaces.IAttribute;
import java.awt.Color;
import java.awt.Point;
import java.util.IdentityHashMap;

/**
 *
 * @author tam
 */
public class Pixel {

    private int color;
    private Point location;
    private IdentityHashMap<Class,IAttribute> attributes;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public short [] getARGB() {
        short [] ret = new short[4];
        ret[0] = (short) ((this.color >> 24) & 0xFF);
        ret[1] = (short) ((this.color >> 16) & 0xFF);
        ret[2] = (short) ((this.color >> 8) & 0xFF);
        ret[3] = (short) (this.color & 0xFF);
        return ret;
    }

    public void setARGB(short [] argb) {
        this.color = 0 | argb[0] << 24 | argb[1] << 16 | argb[2] << 8 | argb[3];
    }

    public float [] getHSB() {
        return Color.RGBtoHSB((this.color >> 16) & 0xFF,
                (this.color >> 8) & 0xFF,
                this.color & 0xFF,
                null);
    }

        public IdentityHashMap<Class, IAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(IdentityHashMap<Class, IAttribute> attributes) {
        this.attributes = attributes;
    }

    public void setAttribute(Class attributeType, IAttribute attribute) {
        this.attributes.put(attributeType, attribute);
    }

    public void removeAttribute(Class attributeType) {
        this.attributes.remove(attributeType);
    }

    public IAttribute getAttribute(Class attributeType) {
        return this.attributes.get(attributeType);
    }

    public Pixel(int color, Point location, IdentityHashMap<Class, IAttribute> attributes) {
        this.color = color;
        this.location = location;
        this.attributes = attributes;
    }
}
