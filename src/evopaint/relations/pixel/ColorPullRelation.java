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
public class ColorPullRelation extends Relation {

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
            ca = new ColorAttribute(0xFF000000);
            a.setAttribute(ColorAttribute.class, ca);
        }

        short [] caARGB = ca.getARGB();
        short [] cbARGB = cb.getARGB();
        
        int index = 0;
        short min = caARGB[index];
        for (int i = 1; i <= 3; i++) {
            if (caARGB[i] < min) {
                min = caARGB[i];
                index = i;
            }
        }

        short missing = (short)(0xFF - caARGB[index]);
        short available = cbARGB[index] > missing ? missing : cbARGB[index];

        caARGB[index] = (short)(caARGB[index] + available);
        cbARGB[index] = (short)(cbARGB[index] - available);

        ca.setARGB(caARGB);
        cb.setARGB(cbARGB);

        return false;
    }

}
