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
    
        // if a has means of being responsible for the relations invoked on it,
        // let us promote this relation since it was successful (law of lazyness)
        RelationChoosingAttribute rca =a.getRelationChoosingAttribute();
        if (rca != null) {
            rca.promote(this.getClass());
        }

        //Logger.log.information("relating %s", this);
        return true;
    }
}
