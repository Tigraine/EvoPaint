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

import evopaint.interfaces.IRandomNumberGenerator;
import java.io.Serializable;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class RelativeCoordinate extends Coordinate implements Serializable {

    private static final int TYPE_SELF = 0;
    private static final int TYPE_NORTH = 1;
    private static final int TYPE_NORTH_EAST = 2;
    private static final int TYPE_EAST = 3;
    private static final int TYPE_SOUTH_EAST = 4;
    private static final int TYPE_SOUTH = 5;
    private static final int TYPE_SOUTH_WEST = 6;
    private static final int TYPE_WEST = 7;
    private static final int TYPE_NORTH_WEST = 8;

    public static final RelativeCoordinate CENTER = new RelativeCoordinate(TYPE_SELF);
    public static final RelativeCoordinate NORTH = new RelativeCoordinate(TYPE_NORTH);
    public static final RelativeCoordinate NORTH_EAST = new RelativeCoordinate(TYPE_NORTH_EAST);
    public static final RelativeCoordinate EAST = new RelativeCoordinate(TYPE_EAST);
    public static final RelativeCoordinate SOUTH_EAST = new RelativeCoordinate(TYPE_SOUTH_EAST);
    public static final RelativeCoordinate SOUTH = new RelativeCoordinate(TYPE_SOUTH);
    public static final RelativeCoordinate SOUTH_WEST = new RelativeCoordinate(TYPE_SOUTH_WEST);
    public static final RelativeCoordinate WEST = new RelativeCoordinate(TYPE_WEST);
    public static final RelativeCoordinate NORTH_WEST = new RelativeCoordinate(TYPE_NORTH_WEST);

    private int type;

    @Override
    public String toString() {
        switch (x) {
            case -1: switch(y) {
                case -1: return "NW";
                case 0: return "W";
                case 1: return "SW";
            } break;
            case 0: switch(y) {
                case -1: return "N";
                case 0: return "me";
                case 1: return "S";
            } break;
            case 1: switch(y) {
                case -1: return "NE";
                case 0: return "E";
                case 1: return "SE";
            } break;
        }
        assert(false);
        return null;
    }

    // preserve singleton through serialization
    public Object readResolve() {
        switch (type) {
            case TYPE_SELF: return RelativeCoordinate.CENTER;
            case TYPE_NORTH: return RelativeCoordinate.NORTH;
            case TYPE_NORTH_EAST: return RelativeCoordinate.NORTH_EAST;
            case TYPE_EAST: return RelativeCoordinate.EAST;
            case TYPE_SOUTH_EAST: return RelativeCoordinate.SOUTH_EAST;
            case TYPE_SOUTH: return RelativeCoordinate.SOUTH;
            case TYPE_SOUTH_WEST: return RelativeCoordinate.SOUTH_WEST;
            case TYPE_WEST: return RelativeCoordinate.WEST;
            case TYPE_NORTH_WEST: return RelativeCoordinate.NORTH_WEST;
            default: assert(false);
        }
        return null;
    }

    public static RelativeCoordinate getRandom(IRandomNumberGenerator rng) {
        int rnd = rng.nextPositiveInt(9);
        switch (rnd) {
            case TYPE_SELF: return RelativeCoordinate.CENTER;
            case TYPE_NORTH: return RelativeCoordinate.NORTH;
            case TYPE_NORTH_EAST: return RelativeCoordinate.NORTH_EAST;
            case TYPE_EAST: return RelativeCoordinate.EAST;
            case TYPE_SOUTH_EAST: return RelativeCoordinate.SOUTH_EAST;
            case TYPE_SOUTH: return RelativeCoordinate.SOUTH;
            case TYPE_SOUTH_WEST: return RelativeCoordinate.SOUTH_WEST;
            case TYPE_WEST: return RelativeCoordinate.WEST;
            case TYPE_NORTH_WEST: return RelativeCoordinate.NORTH_WEST;
            default: assert(false);
        }
        return null;
    }

    private RelativeCoordinate(int type) {
        super(0, 0);
        this.type = type;
        switch (type) {
            case TYPE_SELF: x = 0; y = 0; break;
            case TYPE_NORTH: x = 0; y = -1; break;
            case TYPE_NORTH_EAST: x = 1; y = -1; break;
            case TYPE_EAST: x = 1; y = 0; break;
            case TYPE_SOUTH_EAST: x = 1; y = 1; break;
            case TYPE_SOUTH: x = 0; y = 1; break;
            case TYPE_SOUTH_WEST: x = -1; y = 1; break;
            case TYPE_WEST: x = -1; y = 0; break;
            case TYPE_NORTH_WEST: x = -1; y = -1; break;
            default: assert(false);
        }
        
    }
}
