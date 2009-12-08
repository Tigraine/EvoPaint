/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.relations;

import evopaint.Config;
import evopaint.Relation;
import evopaint.Entity;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.attributes.PartsAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.interfaces.IRandomNumberGenerator;
import java.util.Collection;

/**
 *
 * @author tam
 */
public class PixelPerceptionRelation extends Relation {

    public boolean relate(IRandomNumberGenerator rng) {
        PixelPerceptionAttribute ppa = (PixelPerceptionAttribute)a.getAttribute(PixelPerceptionAttribute.class);
        assert (ppa != null);
        
        ppa.clear();

        return this.relateRecursive(ppa, b);
    }

    private boolean relateRecursive(PixelPerceptionAttribute ppa, Entity b) {
        
        // if it has parts, percieve them
        // XXX as of now, the whole is percieved only when all of its parts are percieved
        PartsAttribute partsAttributeB = (PartsAttribute) b.getAttribute(PartsAttribute.class);
        if (partsAttributeB != null) {
            Collection<Entity> parts = partsAttributeB.getParts();
            boolean ret = true;
            for (Entity part : parts) {
                if (relateRecursive(ppa, part) == false) {
                    ret = false;
                }
            }
            return ret;
        }

        // if b has color and location, we can percieve it
        ColorAttribute cab = (ColorAttribute) b.getAttribute(ColorAttribute.class);
        SpacialAttribute sab = (SpacialAttribute) b.getAttribute(SpacialAttribute.class);
        if (cab != null && sab != null) {
            ppa.setPixel(cab.getColor(), sab.getLocation());
            return true;
        }

        // else this thing is not to be percieved
        //ppa.setPixel(0xFFFFFFFF, sab.getLocation()); //background or stuff..
        return true;
    }

    public PixelPerceptionRelation(Entity a, Entity b) {
        super(a, b);
    }

}

