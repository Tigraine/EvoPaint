/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.matchers;

import evopaint.entities.Pixel;
import evopaint.interfaces.IMatcher;

/**
 *
 * @author tam
 */
public class HSBMatcher implements IMatcher {
    public float match(Pixel a, Pixel b) {
       
        float [] c1hsb = a.getColorAttribute().getHSB();
        float [] c2hsb = b.getColorAttribute().getHSB();

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