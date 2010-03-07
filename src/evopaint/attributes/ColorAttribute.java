/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.interfaces.IAttribute;
import java.awt.Color;

/**
 *
 * @author tam
 */
public class ColorAttribute implements IAttribute {
    private int color;

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(this.color);
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color | 0xFF000000; // XXX cheating Alpha here
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

    public ColorAttribute(int color) {
        this.color = color | 0xFF000000; // XXX cheating Alpha here
    }
}
