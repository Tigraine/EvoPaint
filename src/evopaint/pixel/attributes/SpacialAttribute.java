/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.attributes;

import evopaint.entities.System;
import evopaint.interfaces.IAttribute;
import evopaint.util.ParallaxMap;


/**
 *
 * @author tam
 */
public class SpacialAttribute implements IAttribute {
    int x;
    int y;

    public int getX() {
        return x;
    }

    public void setX(int x, System system) {
        this.x = ParallaxMap.clamp(x, system.getPixels().getWidth());
    }

    public int getY() {
        return y;
    }

    public void setY(int y, System system) {
        this.y = ParallaxMap.clamp(y, system.getPixels().getHeight());
    }

    public SpacialAttribute(int x, int y, System system) {
        assert (system.getPixels().get(x, y) != null);
        this.x = ParallaxMap.clamp(x, system.getPixels().getWidth());
        this.y = ParallaxMap.clamp(y, system.getPixels().getHeight());
    }
}
