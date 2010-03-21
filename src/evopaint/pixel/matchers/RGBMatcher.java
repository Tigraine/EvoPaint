/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.matchers;

import evopaint.pixel.Pixel;
import evopaint.interfaces.IMatcher;

/**
 *
 * @author tam
 */
public class RGBMatcher implements IMatcher {
    public float match(Pixel a, Pixel b) {

        short [] c1argb = a.getPixelColor().getRGB();
        short [] c2argb = b.getPixelColor().getRGB();

        int deltaSum = 0;
        for (int i = 1; i < 4; i++) {
            int delta = 0xFF - Math.abs(c1argb[i] - c2argb[i]);
            deltaSum += delta;
        }

        return ((float)deltaSum) / (0xFF * 3);
    }
}
