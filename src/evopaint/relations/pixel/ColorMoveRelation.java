/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.relations.pixel;

import evopaint.Relation;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.interfaces.IRandomNumberGenerator;

/**
 *
 * @author tam
 */
public class ColorMoveRelation extends Relation {

    @Override
    public boolean relate(IRandomNumberGenerator rng) {
        if (this.b == null) {
            return false;
        }

        // a and b both need spacial means for this relation to work
        // a needs a color, but b needs to be colorless
        SpacialAttribute sa = (SpacialAttribute) a.getAttribute(SpacialAttribute.class);
        SpacialAttribute sb = (SpacialAttribute) b.getAttribute(SpacialAttribute.class);
        ColorAttribute ca = (ColorAttribute) a.getAttribute(ColorAttribute.class);
        ColorAttribute cb = (ColorAttribute) b.getAttribute(ColorAttribute.class);

        if (sa == null || sb == null || ca == null || cb != null) {
            return false;
        }

        a.removeAttribute(ColorAttribute.class);
        b.setAttribute(ColorAttribute.class, ca);

        return true;
    }

}
