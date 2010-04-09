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

package evopaint.pixel.rulebased;

import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.ITargetSelection;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class AbstractTargetSelection implements ITargetSelection {

    private String name;
    protected List<RelativeCoordinate> directions;

    public AbstractTargetSelection(String name, List<RelativeCoordinate> directions) {
        this.name = name;
        this.directions = directions;
    }

    public AbstractTargetSelection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<RelativeCoordinate> getDirections() {
        return directions;
    }

    public void setDirections(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += "of [";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "] ";
        ret += toStringCallback();
        return ret;
    }

    public String toHTML() {
        String ret = new String();
        ret += "of [";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "] ";
        ret += toHTMLCallback();
        return ret;
    }

    protected abstract String toStringCallback();

    protected abstract String toHTMLCallback();
}
