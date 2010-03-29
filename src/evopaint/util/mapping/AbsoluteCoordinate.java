/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util.mapping;

import java.io.Serializable;

/**
 *
 * @author tam
 */
public class AbsoluteCoordinate extends Coordinate implements Serializable {

    @Override
    public String toString() {
        return "(" + x + "/" + y + ")";
    }

    public void move(RelativeCoordinate rc, ParallaxMap map) {
        this.x = ParallaxMap.clamp(x, map.width);
        this.y = ParallaxMap.clamp(y, map.height);
    }

    public AbsoluteCoordinate(int x, int y, ParallaxMap map) {
        super(x, y);
        this.x = ParallaxMap.clamp(x, map.width);
        this.y = ParallaxMap.clamp(y, map.height);
    }

    public AbsoluteCoordinate(AbsoluteCoordinate ac, RelativeCoordinate rc, ParallaxMap map) {
        super(ac.x, ac.y);
        this.x += rc.x;
        this.y += rc.y;
        this.x = ParallaxMap.clamp(x, map.width);
        this.y = ParallaxMap.clamp(y, map.height);
    }

    public AbsoluteCoordinate(AbsoluteCoordinate ac) {
        super(ac.x, ac.y); // no need to clamp because either ac is valid, or we would have screwed up before this point anyways
    }
}
