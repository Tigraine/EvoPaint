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

package evopaint.pixel.rulebased.targeting;

import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class QuantifiedTarget extends Target {
    
    protected int min;
    protected int max;

    public QuantifiedTarget(List<RelativeCoordinate> directions, int min, int max) {
        super(directions);
        this.min = min;
        this.max = max;
        assert (max >= 0 && max <= directions.size());
        assert (min >= 0 && min <= directions.size());
    }

    public QuantifiedTarget() {
    }

    public String getName() {
        return "quantified";
    }

    public int getMin() {
        assert (min >= 0 && min <= directions.size());
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        assert (min >= 0 && min <= directions.size());
    }

    public int getMax() {
        assert (max >= 0 && max <= directions.size());
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        assert (max >= 0 && max <= directions.size());
    }

     @Override
    public String toString() {
        String ret = new String();
        if (min == max) {
            if (min == directions.size()) {
                ret += "all";
            }
            else if (max == 0) {
                ret += "none";
            }
            else {
                ret += min;
            }
        }
        else if (min == 0) {
            ret += "up to " + max;
        }
        else if (max == directions.size()) {
            ret += "at least " + min;
        }
        else {
            ret += min + " to " + max;
        }
        ret += " in ";
        ret += getDirectionsString();
        return ret;
    }

    public String toHTML() {
        return toString();
    }
    
}