/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util.mapping;

/**
 *
 * @author tam
 */
public class AbsoluteCoordinate extends Coordinate {

    @Override
    public String toString() {
        return "(" + x + "/" + y + ")";
    }

    public AbsoluteCoordinate getNeighboring(RelativeCoordinate rc, ParallaxMap map) {
        return new AbsoluteCoordinate(this, rc, map);
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
}
