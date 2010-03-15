/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.attributes;

import evopaint.interfaces.IAttribute;
import java.awt.Color;


/**
 *
 * @author tam
 */
public class ColorAttribute implements IAttribute {
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public short [] getRGB() {
        short [] ret = new short[3];
        //ret[0] = (short) ((this.color >> 24) & 0xFF);
        ret[0] = (short) ((this.color >> 16) & 0xFF);
        ret[1] = (short) ((this.color >> 8) & 0xFF);
        ret[2] = (short) (this.color & 0xFF);
        return ret;
    }

    public void setRGB(short [] argb) {
        this.color = 0 | argb[0] << 16 | argb[1] << 8 | argb[2];
        //this.color = 0 | argb[0] << 24 | argb[1] << 16 | argb[2] << 8 | argb[3];
    }

    public float [] getHSB() {
        return Color.RGBtoHSB((this.color >> 16) & 0xFF,
                (this.color >> 8) & 0xFF,
                this.color & 0xFF,
                null);
    }

    public ColorAttribute(int color) {
        this.color = 0 & color;
    }
}
