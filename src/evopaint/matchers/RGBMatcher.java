/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.matchers;

import evopaint.Entity;
import evopaint.attributes.ColorAttribute;
import evopaint.interfaces.IMatcher;

/**
 *
 * @author tam
 */
public class RGBMatcher implements IMatcher {
    public float match(Entity a, Entity b) {
        ColorAttribute caa = (ColorAttribute) a.getAttribute(ColorAttribute.class);
        ColorAttribute cab = (ColorAttribute) b.getAttribute(ColorAttribute.class);
        if (caa == null || cab == null) {
            return -1;
        }

        short [] c1argb = caa.getARGB();
        short [] c2argb = cab.getARGB();

        int deltaSum = 0;
        for (int i = 1; i < 4; i++) {
            int delta = 0xFF - Math.abs(c1argb[i] - c2argb[i]);
            deltaSum += delta;
        }

        return ((float)deltaSum) / (0xFF * 3);
    }
}
