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
import java.util.ArrayList;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class SingleTarget extends Target {
    protected RelativeCoordinate direction;

    public SingleTarget(RelativeCoordinate direction) {
        this.direction = direction;
    }

    public SingleTarget() {
    }

    public SingleTarget(SingleTarget target) {
        this.direction = target.direction;
    }

    public SingleTarget(IRandomNumberGenerator rng) {
        this.direction = RelativeCoordinate.getRandom(rng);
    }

    public int getType() {
        return Target.SINGLE_TARGET;
    }

    public int countGenes() {
        return 1;
    }

    public void mutate(int mutatedGene, IRandomNumberGenerator rng) {
        assert mutatedGene == 0;
        ArrayList<RelativeCoordinate> unusedDirections =
                new ArrayList<RelativeCoordinate>() {{
                add(RelativeCoordinate.CENTER);
                add(RelativeCoordinate.NORTH);
                add(RelativeCoordinate.NORTH_EAST);
                add(RelativeCoordinate.EAST);
                add(RelativeCoordinate.SOUTH_EAST);
                add(RelativeCoordinate.SOUTH);
                add(RelativeCoordinate.SOUTH_WEST);
                add(RelativeCoordinate.WEST);
                add(RelativeCoordinate.NORTH_WEST);
        }};
        unusedDirections.remove(direction);
        this.direction = unusedDirections.get(rng.nextPositiveInt(unusedDirections.size()));
        return;
    }

    public void mixWith(Target theirTarget, float theirShare, IRandomNumberGenerator rng) {
        if (rng.nextFloat() < theirShare) {
            direction = ((SingleTarget)theirTarget).direction;
        }
    }

    @Override
    public String toString() {
        if (direction == null) {
            return "<no target>";
        }
        return direction.toString();
    }

    public String toHTML() {
        if (direction == null) {
            return "&lt;no target&gt;";
        }
        return direction.toString();
    }

    public RelativeCoordinate getDirection() {
        return direction;
    }

    public void setDirection(RelativeCoordinate direction) {
        this.direction = direction;
    }
    
}
