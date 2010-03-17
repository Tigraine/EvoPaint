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
public class ColorMoveRelation extends PixelRelation {

    @Override
    public boolean relate(World world, IRandomNumberGenerator rng) {

        if (    this.b == null ||
                this.a.getColorAttribute() == null ||
                this.b.getColorAttribute() == null ||
                this.b.getColorAttribute().getColor() != world.getConfiguration().backgroundColor
                ) {
            return false;
        }

        b.getColorAttribute().setColor(a.getColorAttribute().getColor());
        a.getColorAttribute().setColor(world.getConfiguration().backgroundColor);

        return true;
    }

}
