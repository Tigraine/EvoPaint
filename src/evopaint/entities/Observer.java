/*
 * static class to represent the organisms environment as well as the organisms
 * themselves in the flow of time
 */

package evopaint.entities;

import evopaint.*;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.interfaces.IAttribute;
import java.awt.image.BufferedImage;
import java.util.IdentityHashMap;

/**
 *
 * @author tam
 */
public class Observer extends Entity {

    public BufferedImage getPerception() {
        PixelPerceptionAttribute ppa = (PixelPerceptionAttribute) this.attributes.get(PixelPerceptionAttribute.class);
        assert (ppa != null);
        return ppa.getPerception();
    }

    public Observer(IdentityHashMap<Class, IAttribute> attributes, PixelPerceptionAttribute pixelPerceptionAttribute) {
        super(attributes);
        this.attributes.put(PixelPerceptionAttribute.class, (IAttribute)pixelPerceptionAttribute);
    }


}
