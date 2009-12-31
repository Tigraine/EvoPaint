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
public class HSBMatcher implements IMatcher {
    public float match(Entity a, Entity b) {
        ColorAttribute caa = (ColorAttribute) a.getAttribute(ColorAttribute.class);
        ColorAttribute cab = (ColorAttribute) b.getAttribute(ColorAttribute.class);
        if (caa == null || cab == null) {
            return -1;
        }

        float [] c1hsb = caa.getHSB();
        float [] c2hsb = cab.getHSB();

        float deltaSum = 0;
        for (int i = 0; i < 3; i++) {
            float delta = Math.abs(c1hsb[i] - c2hsb[i]);
            if (i == 0) {
                delta = Math.min(delta, 1 - delta);
            }
            deltaSum += delta;
        }

        return  1 - (deltaSum / 3);
    }
}
