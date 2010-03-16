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
        this.color = 0xFF000000 |  color;
    }

    public short [] getRGB() {
        short [] ret = new short[3];
        ret[0] = (short) ((this.color >> 16) & 0xFF);
        ret[1] = (short) ((this.color >> 8) & 0xFF);
        ret[2] = (short) (this.color & 0xFF);
        return ret;
    }

    public void setRGB(short [] rgb) {
        this.color = 0 | rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }

    public float [] getHSB() {
        return Color.RGBtoHSB((this.color >> 16) & 0xFF,
                (this.color >> 8) & 0xFF,
                this.color & 0xFF,
                null);
    }

    // NOTE: javas Color.HSBtoRGB results in 0xFF alpha value
    //      so any alpha channel will be lost after calling this method
    public void setHSB(float [] hsb) {
        assert(hsb.length == 3);
        this.color = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    /* XXX this is currently just 50/50 mixing, but I have a feeling that if
     * we included an evolution finding the optimal mixing percentage, it would
     * turn out to be 50/50 anyways...
     */
    public void mixInRGB(ColorAttribute them) {
        short [] c1rgb = them.getRGB();
        short [] c2rgb = this.getRGB();
        short [] mixrgb = new short[3];

        for (int i = 0; i < 3; i++)
            mixrgb[i] = (short)(Math.min(c1rgb[i], c2rgb[i]) + Math.abs(c1rgb[i] - c2rgb[i]) / 2);

        this.setRGB(mixrgb);
    }

    public void mixInHSB(ColorAttribute them, float theirShare) {
        float[] theirHSB = them.getHSB();
        float[] ourHSB = this.getHSB();
        float[] mixhsb = new float[3];

        mixhsb[0] = mixCyclic(theirHSB[0], ourHSB[0], theirShare);
        mixhsb[1] = mixLinear(theirHSB[1], ourHSB[1], theirShare);
        mixhsb[2] = mixLinear(theirHSB[2], ourHSB[2], theirShare);

        this.setHSB(mixhsb);
    }

    private float mixCyclic(float a, float b, float shareOfA) {
        float ret = 0.0f;
        float min = Math.min(a, b);
        float delta = Math.abs(a - b);
        boolean isWrapped = false;
        if (delta > 1 - delta) {
            isWrapped = true;
            delta = 1 - delta;
        }
        if (min == a) {
            ret = isWrapped ? min - delta * (1 - shareOfA) : min + delta * (1 - shareOfA);
        } else {
            ret = isWrapped ? min - delta * shareOfA : min + delta * shareOfA;
        }
        if (ret < 0) {
            ret = ret + 1;
        }
        return ret;
    }

    private float mixLinear(float a, float b, float shareOfA) {
        float min = Math.min(a, b);
        float delta = Math.abs(a - b);

        if (min == a) {
            return min + delta * (1 - shareOfA);
        } else {
            return min + delta * shareOfA;
        }
    }


    public ColorAttribute(int color) {
        this.color = 0 & color;
    }
}
