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
import evopaint.util.ExceptionHandler;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class SingleTarget implements ITarget {
    protected RelativeCoordinate direction;

    public SingleTarget(RelativeCoordinate direction) {
        this.direction = direction;
    }

    public SingleTarget() {
    }

    public SingleTarget(SingleTarget target) {
        this.direction = target.direction;
    }

    public int getType() {
        return Target.SINGLE_TARGET;
    }

    public void mixWith(ITarget theirTarget, float theirShare, IRandomNumberGenerator rng) {
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
