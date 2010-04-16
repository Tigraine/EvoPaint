/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.util.mapping;

import java.io.Serializable;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class AbsoluteCoordinate extends Coordinate implements Serializable {

    @Override
    public String toString() {
        return "(" + x + "/" + y + ")";
    }

    public void move(RelativeCoordinate rc, ParallaxMap map) {
        this.x = ParallaxMap.clamp(x + rc.x, map.width);
        this.y = ParallaxMap.clamp(y + rc.y, map.height);
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
