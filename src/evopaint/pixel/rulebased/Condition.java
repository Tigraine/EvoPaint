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
import evopaint.pixel.rulebased.conditions.ColorLikenessColorCondition;
import evopaint.pixel.rulebased.conditions.ColorLikenessMyColorCondition;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.ExistenceCondition;
import evopaint.pixel.rulebased.targeting.ConditionMetaTarget;
import evopaint.pixel.rulebased.targeting.ConditionSingleTarget;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.Target;
import java.util.Map;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class Condition implements IHaveTarget {

    protected static final int COLOR_LIKENESS_COLOR = 0;
    protected static final int COLOR_LIKENESS_MY_COLOR = 1;
    protected static final int ENERGY = 2;
    protected static final int EXISTENCE = 3;

    private static final int NUM_CONDITIONS = 4;

    private IConditionTarget target;

    public abstract int getType();

    public int countGenes() {
        return target.countGenes();
    }

    public void mutate(int mutatedGene, IRandomNumberGenerator rng) {
        int targetType = target.getType();
        switch (targetType) {
            case Target.META_TARGET: target = new ConditionMetaTarget((ConditionMetaTarget)target);
            break;
            case Target.SINGLE_TARGET: target = new ConditionSingleTarget((ConditionSingleTarget)target);
            break;
            default: assert (false);
        }
        target.mutate(mutatedGene, rng);
    }

    public void mixWith(Condition theirCondition, float theirShare, IRandomNumberGenerator rng) {
        if (target.getType() == theirCondition.target.getType()) {
            Target newTarget = null;
            int targetType = target.getType();
            switch (targetType) {
                case Target.META_TARGET: newTarget = new ConditionMetaTarget((ConditionMetaTarget)target);
                break;
                case Target.SINGLE_TARGET: newTarget = new ConditionSingleTarget((ConditionSingleTarget)target);
                break;
                default: assert (false);
            }
            newTarget.mixWith((Target)theirCondition.target, theirShare, rng);
            target = (IConditionTarget)newTarget;
        } else {
            if (rng.nextFloat() < theirShare) {
               target = theirCondition.target;
            }
        }
    }

    public static Condition createRandom(IRandomNumberGenerator rng) {
        switch (rng.nextPositiveInt(NUM_CONDITIONS)) {
            case COLOR_LIKENESS_COLOR: return new ColorLikenessColorCondition(rng);
            case COLOR_LIKENESS_MY_COLOR: return new ColorLikenessMyColorCondition(rng);
            case ENERGY: return new EnergyCondition(rng);
            case EXISTENCE: return new ExistenceCondition(rng);
        }
        assert false;
        return null;
    }

    public static Condition copy(Condition condition) {
        int type = condition.getType();
        switch (type) {
            case Condition.COLOR_LIKENESS_COLOR:
                return new ColorLikenessColorCondition(
                        (ColorLikenessColorCondition)condition);
            case Condition.COLOR_LIKENESS_MY_COLOR:
                return new ColorLikenessMyColorCondition(
                        (ColorLikenessMyColorCondition)condition);
            case Condition.ENERGY:
                return new EnergyCondition(
                        (EnergyCondition)condition);
            case Condition.EXISTENCE:
                return new ExistenceCondition(
                        (ExistenceCondition)condition);
            default: assert (false);
                return null;
        }
    }

    protected Condition(IConditionTarget target) {
        this.target = target;
    }

    protected Condition() {
        this.target = new ConditionSingleTarget();
    }

    protected Condition(Condition condition) {
        this.target = condition.target;
    }

    protected Condition(IRandomNumberGenerator rng) {
        int numDirections = rng.nextPositiveInt(8) + 1;
        if (numDirections == 1) {
            this.target = new ConditionSingleTarget(rng);
        } else {
            this.target = new ConditionMetaTarget(numDirections, rng);
        }
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

    public boolean isMet(RuleBasedPixel actor, Configuration configuration) {
        return target.meets(this, actor, configuration);
    }
    
    public abstract boolean isMet(RuleBasedPixel actor, RuleBasedPixel target);

}
