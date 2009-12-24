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

        // mix A's colors into B
        cab.setColor(this.hsbMix(caa, cab, 0.5f)); // XXX there is some hard coding right here
        Config.log.information("relating %s", this);
        return true;
    }

    private int hsbMix(ColorAttribute c1, ColorAttribute c2, float shareOfC1) {
        float [] c1hsb = c1.getHSB();
        float [] c2hsb = c2.getHSB();
        float [] mixhsb = new float[3];

        for (int i = 0; i < 3; i++) {
            float min = Math.min(c1hsb[i], c2hsb[i]);
            float delta = Math.abs(c1hsb[i] - c2hsb[i]);
            if (min == c1hsb[i]) {
                mixhsb[i] = min + delta * (1 - shareOfC1);
            } else {
                mixhsb[i] = min + delta * shareOfC1;
            }
        }

        return Color.HSBtoRGB(mixhsb[0], mixhsb[1], mixhsb[2]);
    }
}
