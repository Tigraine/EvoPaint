/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.relations;

import evopaint.PixelRelation;
import evopaint.entities.World;
import evopaint.interfaces.IRandomNumberGenerator;


/**
 *
 * @author tam
 */
public class ColorCopyRelation extends PixelRelation {

    public boolean relate(World world, IRandomNumberGenerator rng) {

        if (    this.b == null || // b needs to exist
                a.getColorAttribute().getColor() == world.getConfiguration().backgroundColor || // a shall not be empty
                b.getColorAttribute().getColor() == a.getColorAttribute().getColor() // and the colors shall be distinct
                ) {
            return false;
        }

        b.getColorAttribute().setColor(a.getColorAttribute().getColor());
    

        //Logger.log.information("relating %s", this);
        return true;
    }
}
