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
        if (this.b.getColor() == configuration.backgroundColor) {
            return false;
        }

        short [] caARGB = a.getARGB();
        short [] cbARGB = b.getARGB();
        
        int index = 0;
        short min = caARGB[index];
        for (int i = 1; i <= 3; i++) {
            if (caARGB[i] < min) {
                min = caARGB[i];
                index = i;
            }
        }

        short missing = (short)(0xFF - caARGB[index]);
        short available = cbARGB[index] > missing ? missing : cbARGB[index];

        caARGB[index] = (short)(caARGB[index] + available);
        cbARGB[index] = (short)(cbARGB[index] - available);

        a.setARGB(caARGB);
        b.setARGB(cbARGB);

        return false;
    }

}
