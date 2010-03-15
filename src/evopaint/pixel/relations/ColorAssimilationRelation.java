/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.pixel.relations;

import evopaint.Config;
import evopaint.PixelRelation;
import evopaint.entities.Pixel;
import evopaint.interfaces.IRandomNumberGenerator;

import java.awt.Color;

/**
 *
 * @author tam
 */
public class ColorAssimilationRelation extends PixelRelation {

    public boolean relate(Config configuration, IRandomNumberGenerator rng) {
        if (    this.b == null || // b needs to exist
                this.b.getColorAttribute().getColor() == configuration.backgroundColor || // and be not empty
                this.a.getColorAttribute().getColor() == configuration.backgroundColor || // as well as a
                a.getColorAttribute().getColor() == b.getColorAttribute().getColor() // and colors shall be distinct
                ) {
            return false;
        }

        // mix A's colors into B
        b.getColorAttribute().setColor(this.hsbMix(a, b, 0.5f)); // XXX there is some hard coding right here
        //Logger.log.information("relating %s", this);
        return false;
    }

    private int hsbMix(Pixel p1, Pixel p2, float shareOfC1) {
        float[] c1hsb = p1.getColorAttribute().getHSB();
        float[] c2hsb = p2.getColorAttribute().getHSB();
        float[] mixhsb = new float[3];

        mixhsb[0] = mixCyclic(c1hsb[0], c2hsb[0], shareOfC1);
        mixhsb[1] = mixLinear(c1hsb[1], c2hsb[1], shareOfC1);
        mixhsb[2] = mixLinear(c1hsb[2], c2hsb[2], shareOfC1);

        return Color.HSBtoRGB(mixhsb[0], mixhsb[1], mixhsb[2]);
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
}
