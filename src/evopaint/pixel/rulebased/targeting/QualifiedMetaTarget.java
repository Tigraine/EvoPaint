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
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.pixel.rulebased.targeting.qualifiers.ColorLikenessColorQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.ColorLikenessMyColorQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.EnergyQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.ExistenceQualifier;
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
    
    public QualifiedMetaTarget(QualifiedMetaTarget qualifiedMetaTarget) {
        qualifiers = new ArrayList(qualifiedMetaTarget.qualifiers);
    }

    public void mixWith(QualifiedMetaTarget theirTarget, float theirShare, IRandomNumberGenerator rng) {
        super.mixWith(theirTarget, theirShare, rng);

        // cache size() calls for maximum performance
        int ourSize = qualifiers.size();
        int theirSize = theirTarget.qualifiers.size();

        // now mix as many qualifiers as we have in common and add the rest depending
        // on share percentage
        // we have more qualifiers
        if (ourSize > theirSize) {
            int i = 0;
            while (i < theirSize) {
                Qualifier ourQualifier = qualifiers.get(i);
                Qualifier theirQualifier = theirTarget.qualifiers.get(i);
                if (ourQualifier.getType() == theirQualifier.getType()) {
                    Qualifier newQualifier = null;
                    int type = ourQualifier.getType();
                    switch (type) {
                        case Qualifier.COLOR_LIKENESS_COLOR:
                            newQualifier = new ColorLikenessColorQualifier(
                                    (ColorLikenessColorQualifier)theirQualifier);
                            break;
                        case Qualifier.COLOR_LIKENESS_MY_COLOR:
                            newQualifier = new ColorLikenessMyColorQualifier(
                                    (ColorLikenessMyColorQualifier)theirQualifier);
                            break;
                        case Qualifier.ENERGY:
                            newQualifier = new EnergyQualifier(
                                    (EnergyQualifier)theirQualifier);
                            break;
                        case Qualifier.EXISTENCE:
                            newQualifier = new ExistenceQualifier(
                                    (ExistenceQualifier)theirQualifier);
                        default: assert (false);
                    }
                    newQualifier.mixWith(theirQualifier, theirShare, rng);
                    qualifiers.set(i, newQualifier);
                } else {
                    if (rng.nextFloat() < theirShare) {
                        qualifiers.set(i, theirQualifier);
                    }
                }
                i++;
            }
            int removed = 0;
            while (i < ourSize - removed) {
                if (rng.nextFloat() < theirShare) {
                    qualifiers.remove(i);
                    removed ++;
                } else {
                    i++;
                }
            }
        } else { // they have more qualifiers or we have an equal number of conditions
           int i = 0;
            while (i < ourSize) {
                Qualifier ourQualifier = qualifiers.get(i);
                Qualifier theirQualifier = theirTarget.qualifiers.get(i);
                if (ourQualifier.getType() == theirQualifier.getType()) {
                    Qualifier newQualifier = null;
                    int type = ourQualifier.getType();
                    switch (type) {
                        case Qualifier.COLOR_LIKENESS_COLOR:
                            newQualifier = new ColorLikenessColorQualifier(
                                    (ColorLikenessColorQualifier)theirQualifier);
                            break;
                        case Qualifier.COLOR_LIKENESS_MY_COLOR:
                            newQualifier = new ColorLikenessMyColorQualifier(
                                    (ColorLikenessMyColorQualifier)theirQualifier);
                            break;
                        case Qualifier.ENERGY:
                            newQualifier = new EnergyQualifier(
                                    (EnergyQualifier)theirQualifier);
                            break;
                        case Qualifier.EXISTENCE:
                            newQualifier = new ExistenceQualifier(
                                    (ExistenceQualifier)theirQualifier);
                        default: assert (false);
                    }
                    newQualifier.mixWith(theirQualifier, theirShare, rng);
                    qualifiers.set(i, newQualifier);
                } else {
                    if (rng.nextFloat() < theirShare) {
                        qualifiers.set(i, theirQualifier);
                    }
                }
                i++;
            }
            while (i < theirSize) {
                if (rng.nextFloat() < theirShare) {
                    qualifiers.add(theirTarget.qualifiers.get(i));
                }
                i++;
            }
        }
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

    public RelativeCoordinate getCandidate(RuleBasedPixel actor, Configuration configuration) {
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
