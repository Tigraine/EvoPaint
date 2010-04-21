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
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ConditionSingleTarget
        extends SingleTarget implements IConditionTarget {

    public ConditionSingleTarget(RelativeCoordinate direction) {
        super(direction);
    }

    public ConditionSingleTarget() {
    }

    public ConditionSingleTarget(ConditionSingleTarget conditionTarget) {
        super(conditionTarget);
    }

    public ConditionSingleTarget(IRandomNumberGenerator rng) {
        super(rng);
    }

    public boolean meets(Condition condition, RuleBasedPixel actor, Configuration configuration) {
        RuleBasedPixel target = configuration.world.get(actor.getLocation(), direction);
        return condition.isMet(actor, target);
    }
    
}
