/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.attributes.PartnerSelectionAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.entities.World;
import evopaint.interfaces.IRandomNumberGenerator;
import java.awt.Point;

/**
 *
 * @author tam
 */
public abstract class Relation {
    protected Entity a;
    protected Entity b;
    protected static int radiusOfInfluence = 1;

    public abstract boolean relate(IRandomNumberGenerator rng);
    
    public void reset(World world, IRandomNumberGenerator rng) {
        SpacialAttribute sa = (SpacialAttribute) world.getAttribute(SpacialAttribute.class);
        assert(sa != null);
        Point location = rng.nextLocation(sa.getDimension());
        this.a = world.locationToEntity(location);
        this.resetB(world, rng);
    }

    public void resetB(World world, IRandomNumberGenerator rng) {
        // if A has the ability to select its own partner, do it
        PartnerSelectionAttribute psa =
                (PartnerSelectionAttribute) this.a.getAttribute(PartnerSelectionAttribute.class);
        if (psa != null) {
            this.b = psa.findPartner(world, a, radiusOfInfluence, rng);
        } else {
            // else choose B from A's environment
            SpacialAttribute sa = (SpacialAttribute) this.a.getAttribute(SpacialAttribute.class);
            assert (sa != null);
            Point newLocation = new Point(sa.getOrigin());

            // TODO: points close to A need a quadratically higher chance
            // to be chosen
            newLocation.translate(rng.nextPositiveInt(2*radiusOfInfluence+1) - radiusOfInfluence,
                    rng.nextPositiveInt(2*radiusOfInfluence+1) - radiusOfInfluence);
            this.b = world.locationToEntity(newLocation);
        }
    }

    public void setA(Entity a) {
        this.a = a;
    }

    public Relation(Entity a, Entity b) {
        this.a = a;
        this.b = b;
    }

    public Relation() {}
}
