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

import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class QuantifiedMetaTarget extends MetaTarget {
    
    protected int min;
    protected int max;

    public QuantifiedMetaTarget(List<RelativeCoordinate> directions, int min, int max) {
        super(directions);
        this.min = min;
        this.max = max;
    }

    public QuantifiedMetaTarget() {
    }

    public QuantifiedMetaTarget(QuantifiedMetaTarget quantifiedMetaTarget) {
        super(quantifiedMetaTarget);
        this.min = quantifiedMetaTarget.min;
        this.max = quantifiedMetaTarget.max;
    }

    public QuantifiedMetaTarget(int numDirections, IRandomNumberGenerator rng) {
        super(numDirections, rng);
        
        if (directions.size() == 0) {
            this.min = 0;
            this.max = 0;
            return;
        }
        
        this.min = rng.nextPositiveInt(directions.size());
        this.max = rng.nextPositiveInt(directions.size());
    }

    @Override
    public int countGenes() {
        return super.countGenes() + 2;
    }

    @Override
    public void mutate(int mutatedGene, IRandomNumberGenerator rng) {
        int numGenesSuper = super.countGenes();
        if (mutatedGene < numGenesSuper) {
            super.mutate(mutatedGene, rng);
            return;
        }
        mutatedGene -= numGenesSuper;

        switch (mutatedGene) {
            case 0: min = directions.size() == 0 ? 0 : rng.nextPositiveInt(directions.size());
            return;
            case 1: max = directions.size() == 0 ? 0 : rng.nextPositiveInt(directions.size());
            return;
        }
        assert false; // we have an error in our mutatedGene calculation
    }

    @Override
    public void mixWith(Target theirTarget, float theirShare, IRandomNumberGenerator rng) {
        QuantifiedMetaTarget q = (QuantifiedMetaTarget)theirTarget;
        super.mixWith(theirTarget, theirShare, rng);
        if (rng.nextFloat() < theirShare) {
            min = q.min;
        }
        if (rng.nextFloat() < theirShare) {
            max = q.max;
        }
    }

    public String getName() {
        return "quantified";
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
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
        ret += " of ";
        ret += super.toString();
        return ret;
    }

    @Override
    public String toHTML() {
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
        ret += " of ";
        ret += super.toHTML();
        return ret;
    }
    
}
