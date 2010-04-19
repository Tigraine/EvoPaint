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
import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.targeting.ConditionTarget;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.util.ExceptionHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class Condition implements IHaveTarget, ICopyable {

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
                target = (IConditionTarget)theirCondition.target.getCopy();
            }
        }
    }

    public Condition(IConditionTarget target) {
        this.target = target;
    }

    public Condition() {
        this.target = new ConditionTarget();
    }

    public IConditionTarget getTarget() {
        return target;
    }

    public void setTarget(ITarget target) {
        this.target = (IConditionTarget)target;
    }

    public Condition getCopy() {
        Condition newCondition = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newCondition = (Condition) in.readObject();
        } catch (ClassNotFoundException ex) {
            ExceptionHandler.handle(ex, true);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex, true);
        }
        return newCondition;
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
