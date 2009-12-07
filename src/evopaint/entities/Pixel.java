/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.entities;

import evopaint.*;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.interfaces.IAttribute;
import java.util.IdentityHashMap;

/**
 *
 * @author tam
 */
public class Pixel extends Entity {

    public Pixel(IdentityHashMap<Class, IAttribute> attributes, ColorAttribute pa, SpacialAttribute sa) {
        super(attributes);
        this.attributes.put(ColorAttribute.class, pa);
        this.attributes.put(SpacialAttribute.class, sa);
    }
}
