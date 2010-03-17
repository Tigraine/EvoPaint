/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.relations;

import evopaint.entities.World;
import evopaint.interfaces.IRandomNumberGenerator;


/**
 *
 * @author tam
 */
public class PixelMoveRelation extends ColorMoveRelation {

    @Override
    public boolean relate(World world, IRandomNumberGenerator rng) {

        if (!super.relate(world, rng)) {
            return false;
        }

       // a.setLocation(b.getLocation());
       // a.clear();

        return true;
    }
}
