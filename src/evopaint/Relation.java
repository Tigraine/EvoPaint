/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.attributes.PartnerSelectionAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.entities.World;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.Log;
import java.awt.Point;

/**
 *
 * @author tam
 */
public abstract class Relation {
    protected Entity a;
    protected Entity b;
    protected int radiusOfInfluence = 1;
    protected int failCounter = 0;

    public abstract boolean relate(IRandomNumberGenerator rng);

    @Override
    public String toString() {
        String ret = this.getClass().getSimpleName();

        if (Config.logVerbosity >= Log.Verbosity.VERBOSE) {
            ret += "(" + this.hashCode() + ")";
        }
        
        if  (Config.logVerbosity >= Log.Verbosity.VERBOSE) {
                ret += " A={" + this.a + "}, B={" + this.b + "}";
        }

        return ret;
    }
    
    public void reset(IRandomNumberGenerator rng) {
        this.a = World.locationToEntity(rng.nextLocation());
        this.resetB(rng);
    }

    public void resetB(IRandomNumberGenerator rng) {
        // if A has the ability to select its own partner, do it
        PartnerSelectionAttribute psa =
                (PartnerSelectionAttribute) this.a.getAttribute(PartnerSelectionAttribute.class);
        if (psa != null) {
            this.b = psa.findPartner(a, radiusOfInfluence, rng);
            if (this.b == null) {
                this.failCounter++;
            }
        } else {
            // else choose B from A's environment
            SpacialAttribute sa = (SpacialAttribute) this.a.getAttribute(SpacialAttribute.class);
            assert (sa != null);
            Point newLocation = new Point(sa.getLocation());
            newLocation.translate(rng.nextPositiveInt(2*radiusOfInfluence+1) - radiusOfInfluence,
                    rng.nextPositiveInt(2*radiusOfInfluence+1) - radiusOfInfluence);
            this.b = World.locationToEntity(newLocation);
        }
    }

    public void setA(Entity a) {
        this.a = a;
    }

    public int getFailCounter() {
        return this.failCounter;
    }

    public Relation(Entity a, Entity b) {
        this.a = a;
        this.b = b;
    }

    public Relation() {}
}
