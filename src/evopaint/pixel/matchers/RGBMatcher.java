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
public class RGBMatcher implements IMatcher {
    public float match(Pixel a, Pixel b) {

        short [] c1argb = a.getColorAttribute().getRGB();
        short [] c2argb = b.getColorAttribute().getRGB();

        int deltaSum = 0;
        for (int i = 1; i < 4; i++) {
            int delta = 0xFF - Math.abs(c1argb[i] - c2argb[i]);
            deltaSum += delta;
        }

        return ((float)deltaSum) / (0xFF * 3);
    }
}