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
public class ColorAssimilationRelation extends PixelRelation {

    public boolean relate(Config configuration, IRandomNumberGenerator rng) {
        if (    this.b == null || // b needs to exist
                this.b.getColorAttribute().getColor() == configuration.backgroundColor || // and be not empty
                this.a.getColorAttribute().getColor() == configuration.backgroundColor || // as well as a
                a.getColorAttribute().getColor() == b.getColorAttribute().getColor() // and colors shall be distinct
                ) {
            return false;
        }

        // mix A's colors into B
        b.getColorAttribute().mixInHSB(a.getColorAttribute(), 0.5f); // XXX there is some hard coding right here

        return true;
    }

    
}
