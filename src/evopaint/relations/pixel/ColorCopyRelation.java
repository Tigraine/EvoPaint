/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.relations.pixel;

import evopaint.Config;
import evopaint.Entity;
import evopaint.attributes.ColorAttribute;
import evopaint.Relation;
import evopaint.attributes.RelationChoosingAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.Log;
import java.awt.Point;

/**
 *
 * @author tam
 */
public class ColorCopyRelation extends Relation {
    public static final int maxDistance = 5;

    public boolean relate(IRandomNumberGenerator rng) {
        if (this.b == null) {
            if (Config.logLevel >= Log.Level.INFORMATION) {
                Config.log.information("relation invalid (no partner) %s",  this);
            }
            return false;
        }
        
        // a and b both need spacial means for this relation to work
        SpacialAttribute sa = (SpacialAttribute) a.getAttribute(SpacialAttribute.class);
        if (sa == null) {
            if (Config.logLevel >= Log.Level.INFORMATION) {
                Config.log.information("invalid relation (A has no spacial means) %s", this);
            }
            return false;
        }
        SpacialAttribute sb = (SpacialAttribute) b.getAttribute(SpacialAttribute.class);
        if (sb == null) {
            Config.log.information("invalid relation (has no spacial means) %s", this);
            return false;
        }

        // a and b need to stay within a maximum distance to each other
        Point locationA = sa.getLocation();
        Point locationB = sb.getLocation();
        double distance = Point.distance(locationA.x, locationA.y, locationB.x, locationB.y);
        // TODO: fix distance for clamped world

        if (distance > ColorCopyRelation.maxDistance) {
            Config.log.information("invalid relation (distance between A and B exceeded tolerance) %s", this);
            return false;
        }

        // radius of influence decreases radial (exponent 2)
        if (rng.nextDouble() < Math.pow(distance / ColorCopyRelation.maxDistance, 2)) {
            Config.log.information("not relating (simulating radial decrease in power) %s", this);
        }

        // now let us get to copying colors, therefore a needs a color
        ColorAttribute ca = (ColorAttribute) a.getAttribute(ColorAttribute.class);
        if (ca == null) {
            Config.log.information("invalid relation (A has no color) %s", this);
            return false;
        }
        
        // copy color to b. if b does not have a color yet, we create one
        ColorAttribute cb = (ColorAttribute) b.getAttribute(ColorAttribute.class);
        if (cb == null) {
            cb = new ColorAttribute(ca.getColor());
            b.setAttribute(ColorAttribute.class, cb);
        } else {
            if (ca.getColor() == cb.getColor()) {
                 Config.log.information("invalid relation (target of same color) %s", this);
                 return false;
            }
            cb.setColor(ca.getColor());
        }
        
        // if a has means of being responsible for the relations invoked on it,
        // let us promote this relation since it was successful (law of lazyness)
        RelationChoosingAttribute rca = (RelationChoosingAttribute) a.getAttribute(RelationChoosingAttribute.class);
        if (rca != null) {
            rca.promote(this.getClass());
        }

        Config.log.information("relating %s", this);
        return true;
    }

    public ColorCopyRelation(Entity a, Entity b) {
        super(a, b);
    }

    public ColorCopyRelation() {}
}
