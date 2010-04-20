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

import evopaint.pixel.rulebased.targeting.IHaveTarget;
import evopaint.Configuration;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.targeting.ConditionTarget;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import java.util.Map;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class Condition implements IHaveTarget {

    protected static final int COLOR_LIKENESS_CONDITION_COLOR = 0;
    protected static final int COLOR_LIKENESS_CONDITION_MY_COLOR = 1;
    protected static final int ENERGY = 2;
    protected static final int EXISTENCE = 3;
    protected static final int TRUE = 4;

    private IConditionTarget target;

    public abstract int getType();

    public void mixWith(Condition theirCondition, float theirShare, IRandomNumberGenerator rng) {
        if (getType() == theirCondition.target.getType()) {
            target.mixWith(theirCondition.target, theirShare, rng);
        } else {
            if (rng.nextFloat() < theirShare) {
               target = theirCondition.target;
            }
        }
    }

    public Condition(IConditionTarget target) {
        this.target = target;
    }

    public Condition() {
        this.target = new ConditionTarget();
    }

    public Condition(Condition condition) {
        this.target = condition.target;
    }

    public IConditionTarget getTarget() {
        return target;
    }

    public void setTarget(ITarget target) {
        this.target = (IConditionTarget)target;
    }

    public Map<String, String>addParametersString(Map<String, String> parametersMap) {
        return parametersMap;
    }

    public Map<String, String>addParametersHTML(Map<String, String> parametersMap) {
        return parametersMap;
    }

    public boolean isMet(Pixel actor, Configuration configuration) {
        return target.meets(this, actor, configuration);
    }
    
    public abstract boolean isMet(Pixel actor, Pixel target);

}
