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
                this.b.getColorAttribute().getColor() == configuration.backgroundColor || // and not be empty
                this.a.getColorAttribute().getColor() == configuration.backgroundColor // and so does a
                ) {
            return false;
        }

        NeuronalAttribute na = a.getNeuronalAttribute();
        if (na == null) {
            return false;
        }

        short [] aRGB = a.getColorAttribute().getRGB();
        byte i = na.getIndex();
        if (aRGB[i] <= na.getThreshold()) {
            return false;
        }

        short [] bRGB = b.getColorAttribute().getRGB();
        short maximumCapacity = (short)(0xFF - bRGB[i]);
        short amount = maximumCapacity <= aRGB[i] ? maximumCapacity : aRGB[i];

        bRGB[i] += amount;
        aRGB[i] -= amount;

        a.getColorAttribute().setRGB(aRGB);
        b.getColorAttribute().setRGB(bRGB);

        return true;
    }
}
