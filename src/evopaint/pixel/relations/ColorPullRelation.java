/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.relations;

import evopaint.Config;
import evopaint.PixelRelation;
import evopaint.interfaces.IRandomNumberGenerator;

/**
 *
 * @author tam
 */
public class ColorPullRelation extends PixelRelation {

    @Override
    public boolean relate(Config configuration, IRandomNumberGenerator rng) {

        if (    this.b == null || // b must exist
                this.b.getColor() == configuration.backgroundColor || // and not be empty
                this.a.getColor() == configuration.backgroundColor // just as a
                ) {
            return false;
        }

        short [] aRGB = a.getRGB();
        short [] bRGB = b.getRGB();


        int index = 0;
        short min = aRGB[index];
        for (int i = 1; i < 3; i++) {
            if (aRGB[i] < min) {
                min = aRGB[i];
                index = i;
            } else if (aRGB[i] == min) {
                if (rng.nextBoolean()) {
                    min = aRGB[i];
                    index = i;
                }
            }
        }

        short missing = (short)(0xFF - aRGB[index]);
        short available = bRGB[index] > missing ? missing : bRGB[index];

        aRGB[index] = (short)(aRGB[index] + available);
        bRGB[index] = (short)(bRGB[index] - available);

        a.setRGB(aRGB);
        b.setRGB(bRGB);

        return true;
    }

}
