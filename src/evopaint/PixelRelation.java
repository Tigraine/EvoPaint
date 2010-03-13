/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.attributes.PartnerSelectionAttribute;
import evopaint.entities.Pixel;
import evopaint.entities.World;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.interfaces.IRelation;
import java.awt.Point;

/**
 *
 * @author tam
 */
public abstract class PixelRelation implements IRelation {
    protected Pixel a;
    protected Pixel b;
    protected static int radiusOfInfluence = 1;

    public abstract boolean relate(Config configuration, IRandomNumberGenerator rng);
    
    public void reset(World world, IRandomNumberGenerator rng) {
        Point location = rng.nextLocation(world.getDimension());
        this.a = world.locationToPixel(location);
        this.resetB(world, rng);
    }

    public void resetB(World world, IRandomNumberGenerator rng) {
        // if A has the ability to select its own partner, do it
        PartnerSelectionAttribute psa =
                (PartnerSelectionAttribute) this.a.getAttribute(PartnerSelectionAttribute.class);
        if (psa != null) {
            this.b = psa.findPartner(world, this.a, radiusOfInfluence, rng);
        } else {
            // else choose B from A's environment
            Point newLocation = new Point(a.getLocation());

            // TODO: points close to A need a quadratically higher chance
            // to be chosen
            newLocation.translate(rng.nextPositiveInt(2*radiusOfInfluence+1) - radiusOfInfluence,
                    rng.nextPositiveInt(2*radiusOfInfluence+1) - radiusOfInfluence);
            this.b = world.locationToPixel(newLocation);
        }
    }

    public void setA(Pixel a) {
        this.a = a;
    }

    public PixelRelation(Pixel a, Pixel b) {
        this.a = a;
        this.b = b;
    }

    public PixelRelation() {}
}
