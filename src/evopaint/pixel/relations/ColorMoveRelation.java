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
                this.b.getColorAttribute().getColor() != configuration.backgroundColor || // and be empty
                this.a.getColorAttribute().getColor() == configuration.backgroundColor // but not a
                ) {
            return false;
        }

        b.getColorAttribute().setColor(a.getColorAttribute().getColor());
        a.getColorAttribute().setColor(configuration.backgroundColor);

        return true;
    }

}
