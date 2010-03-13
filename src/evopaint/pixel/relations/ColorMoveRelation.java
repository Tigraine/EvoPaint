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
public class ColorMoveRelation extends PixelRelation {

    @Override
    public boolean relate(Config configuration, IRandomNumberGenerator rng) {

        if (    this.b == null || // b needs to exist
                this.b.getColor() != configuration.backgroundColor || // and be empty
                this.a.getColor() == configuration.backgroundColor // but not a
                ) {
            return false;
        }

        b.setColor(a.getColor());
        a.setColor(configuration.backgroundColor);

        return true;
    }

}
