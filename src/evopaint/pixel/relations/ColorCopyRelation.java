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

        // a and b need to stay within a maximum distance to each other
        Point locationA = a.getLocation();
        Point locationB = b.getLocation();


        // now let us get to copying colors, therefore a needs a color
        //ColorAttribute ca = (ColorAttribute) a.getAttribute(ColorAttribute.class);
        if (a.getColor() == configuration.backgroundColor) {

            //Logger.log.information("invalid relation (A has no color) %s", this);
            return false;
        }

        b.setColor(a.getColor());
    
        // if a has means of being responsible for the relations invoked on it,
        // let us promote this relation since it was successful (law of lazyness)
        RelationChoosingAttribute rca = (RelationChoosingAttribute) a.getAttribute(RelationChoosingAttribute.class);
        if (rca != null) {
            rca.promote(this.getClass());
        }

        //Logger.log.information("relating %s", this);
        return true;
    }

    public ColorCopyRelation(Pixel a, Pixel b) {
        super(a, b);
    }

    public ColorCopyRelation() {}
}
