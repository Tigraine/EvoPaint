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

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class QualifiedMetaTarget
        extends MetaTarget implements INamed {

    protected List<Qualifier> qualifiers;

    public QualifiedMetaTarget(List<RelativeCoordinate> directions, List<Qualifier> qualifiers) {
        super(directions);
        this.qualifiers = qualifiers;
    }

    public QualifiedMetaTarget(List<RelativeCoordinate> directions) {
        super(directions);
    }

    public QualifiedMetaTarget() {
    }

    public String getName() {
        return "qualified";
    }

    public List<Qualifier> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<Qualifier> qualifiers) {
        this.qualifiers = qualifiers;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += "one of ";
        ret += super.toString();
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = new String();
        ret += "one of ";
        ret += super.toHTML();
        return ret;
    }

    public RelativeCoordinate getCandidate(Pixel actor, Configuration configuration) {
        List<RelativeCoordinate> qualifyingDirections = new ArrayList(directions);
        for (Qualifier q : qualifiers) {
            qualifyingDirections = q.getCandidates(actor, qualifyingDirections, configuration);
        }
        if (qualifyingDirections.size() == 1) {
            return qualifyingDirections.get(0);
        }
        if (qualifyingDirections.size() == 0) {
            return null;
        }
        return qualifyingDirections.get(configuration.rng.nextPositiveInt(qualifyingDirections.size()));
    }
    
}
