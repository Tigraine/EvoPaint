/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.relations.pixel;

import evopaint.Config;
import evopaint.Entity;
import evopaint.Relation;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.PartnerSelectionAttribute;
import evopaint.interfaces.IObjectRenderer;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.Log;
import java.awt.Color;

/**
 *
 * @author tam
 */
public class ColorAssimilationRelation extends Relation {

    public boolean relate(IRandomNumberGenerator rng) {
        //this.resetB(rng);

        if (this.b == null) {
            Config.log.information("relation invalid (no partner) %s", this);
            return false;
        }

        // both entities need to have color to be able to mix
        ColorAttribute caa = (ColorAttribute) a.getAttribute(ColorAttribute.class);
        ColorAttribute cab = (ColorAttribute) b.getAttribute(ColorAttribute.class);
        if (caa == null || cab == null) {
            Config.log.information("relation invalid (color gone) A={%s}, B={%s}", this.a, this.b);
            return false;
        }

        // if the color already is the same, we might want to use this relation elsewhere
        if (caa.getColor() == cab.getColor()) {
            Config.log.information("relation invalid (target has same color) A={%s}, B={%s}", this.a, this.b);
            return false;
        }

        // mix A's colors into B
        cab.setColor(this.hsbMix(caa, cab, 0.5f)); // XXX there is some hard coding right here
        Config.log.information("relating %s", this);
        return true;
    }

    private int hsbMix(ColorAttribute c1, ColorAttribute c2, float shareOfC1) {
        float[] c1hsb = c1.getHSB();
        float[] c2hsb = c2.getHSB();
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
