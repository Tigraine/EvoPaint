/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util.mapping;

import evopaint.interfaces.IRandomNumberGenerator;

/**
 *
 * @author tam
 */
public class RelativeCoordinate extends Coordinate {
    public static final int SPECIAL_ALL = 0;
    public static final int SPECIAL_ANY = 1;

    public static final RelativeCoordinate SELF = new RelativeCoordinate(0, 0);
    public static final RelativeCoordinate NORTH = new RelativeCoordinate(0, -1);
    public static final RelativeCoordinate NORTH_EAST = new RelativeCoordinate(1, -1);
    public static final RelativeCoordinate EAST = new RelativeCoordinate(1, 0);
    public static final RelativeCoordinate SOUTH_EAST = new RelativeCoordinate(1, 1);
    public static final RelativeCoordinate SOUTH = new RelativeCoordinate(0, 1);
    public static final RelativeCoordinate SOUTH_WEST = new RelativeCoordinate(-1, 1);
    public static final RelativeCoordinate WEST = new RelativeCoordinate(-1, 0);
    public static final RelativeCoordinate NORTH_WEST = new RelativeCoordinate(-1, -1);

    public static final RelativeCoordinate ALL = new RelativeCoordinate(SPECIAL_ALL);
    public static final RelativeCoordinate ANY = new RelativeCoordinate(SPECIAL_ANY);
    
    private String name;
    private int special;

    @Override
    public String toString() {
        return name;
    }

    public int getSpecial() {
        return special;
    }

    private RelativeCoordinate(int special) {
        super(0, 0);
        this.special = special;
        switch (special) {
            case SPECIAL_ALL: this.name = "all";
            break;
            case SPECIAL_ANY: this.name = "any";
            break;
        }
    }

    public RelativeCoordinate(boolean includingHere, IRandomNumberGenerator rng) {
        this(rng.nextPositiveInt(3) - 1, rng.nextPositiveInt(3) - 1);
        if (includingHere) {
            this.name = "random(" + this.name + ")";
        } else {
            this.name = "random neighbor(" + this.name + ")";
        }
    }

    private RelativeCoordinate(int x, int y) {
        super(x, y);
        switch (x) {
            case -1: switch(y) {
                case -1: name = "north west"; break;
                case 0: name = "west"; break;
                case 1: name = "south west"; break;
            } break;
            case 0: switch(y) {
                case -1: name = "north"; break;
                case 0: name = "self"; break;
                case 1: name = "south"; break;
            } break;
            case 1: switch(y) {
                case -1: name = "north east"; break;
                case 0: name = "east"; break;
                case 1: name = "south east"; break;
            } break;
        }
    }
}
