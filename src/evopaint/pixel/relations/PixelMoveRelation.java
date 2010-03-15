/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.relations;

import evopaint.Config;
import evopaint.interfaces.IRandomNumberGenerator;


/**
 *
 * @author tam
 */
public class PixelMoveRelation extends ColorMoveRelation {

    @Override
    public boolean relate(Config configuration, IRandomNumberGenerator rng) {

        if (!super.relate(configuration, rng)) {
            return false;
        }

       // a.setLocation(b.getLocation());
       // a.clear();

        return true;
    }
}
