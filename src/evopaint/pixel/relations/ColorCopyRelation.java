/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.relations;

import evopaint.Config;
import evopaint.PixelRelation;
import evopaint.pixel.attributes.RelationChoosingAttribute;
import evopaint.entities.Pixel;
import evopaint.interfaces.IRandomNumberGenerator;

import java.awt.Point;

/**
 *
 * @author tam
 */
public class ColorCopyRelation extends PixelRelation {

    public boolean relate(Config configuration, IRandomNumberGenerator rng) {

        if (    this.b == null || // b needs to exist
                a.getColorAttribute().getColor() == configuration.backgroundColor || // a shall not be empty
                b.getColorAttribute().getColor() == a.getColorAttribute().getColor() // and the colors shall be distinct
                ) {
            return false;
        }

        // a and b need to stay within a maximum distance to each other
        Point locationA = a.getLocation();
        Point locationB = b.getLocation();

        b.getColorAttribute().setColor(a.getColorAttribute().getColor());
    

        //Logger.log.information("relating %s", this);
        return true;
    }
}
