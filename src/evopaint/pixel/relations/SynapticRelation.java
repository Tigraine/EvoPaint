/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.relations;

import evopaint.Config;
import evopaint.PixelRelation;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.attributes.NeuronalAttribute;

/**
 *
 * @author tam
 */
public class SynapticRelation extends PixelRelation {

    @Override
    public boolean relate(Config configuration, IRandomNumberGenerator rng) {

        if (    this.b == null || // b needs to exist
                this.b.getColor() == configuration.backgroundColor || // and not be empty
                this.a.getColor() == configuration.backgroundColor // and so does a
                ) {
            return false;
        }

        NeuronalAttribute na = (NeuronalAttribute)a.getAttribute(NeuronalAttribute.class);
        if (na == null) {
            return false;
        }

        short [] aRGB = a.getRGB();
        byte i = na.getIndex();
        if (aRGB[i] <= na.getThreshold()) {
            return false;
        }

        short [] bRGB = b.getRGB();
        short maximumCapacity = (short)(0xFF - bRGB[i]);
        short amount = maximumCapacity <= aRGB[i] ? maximumCapacity : aRGB[i];

        bRGB[i] += amount;
        aRGB[i] -= amount;

        a.setRGB(aRGB);
        b.setRGB(bRGB);

        return true;
    }
}
