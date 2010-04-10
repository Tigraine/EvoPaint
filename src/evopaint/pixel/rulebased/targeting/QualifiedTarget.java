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

import evopaint.pixel.rulebased.targeting.qualifiers.NonExistenceQualifier;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class QualifiedTarget
        extends Target {

    protected Qualifier qualifier;

    public QualifiedTarget(List<RelativeCoordinate> directions, Qualifier qualifier) {
        super(directions);
        this.qualifier = qualifier;
    }

    public QualifiedTarget() {
        this.qualifier = new NonExistenceQualifier();
    }

    public String getName() {
        return "qualified";
    }

    public Qualifier getQualifier() {
        return qualifier;
    }

    public void setQualifier(Qualifier qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += qualifier.getName();
        ret += " in ";
        ret += getDirectionsString();
        return ret;
    }

    public String toHTML() {
        String ret = new String();
        ret += qualifier.getName();
        ret += " in ";
        ret += getDirectionsString();
        return ret;
    }
    
}
