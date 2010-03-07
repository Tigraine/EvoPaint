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
public class TakeRedRelation extends Relation {

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

        if (sa == null || sb == null || cb == null) {
            return false;
        }

        if (ca == null) {
            ca = new ColorAttribute(0x00000000);
            a.setAttribute(ColorAttribute.class, ca);
        }

        short [] caARGB = ca.getARGB();
        short [] cbARGB = cb.getARGB();

        short missing = (short)(0xFF - caARGB[1]);
        short available = cbARGB[1] > missing ? missing : cbARGB[1];

        caARGB[1] = (short)(caARGB[1] + available);
        cbARGB[1] = (short)(cbARGB[1] - available);

        ca.setARGB(caARGB);
        cb.setARGB(cbARGB);

        return true;
    }

}
