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
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ActionMetaTarget extends QualifiedMetaTarget implements IActionTarget {

    public ActionMetaTarget(List<RelativeCoordinate> directions, List<Qualifier> qualifiers) {
        super(directions, qualifiers);
    }

    public ActionMetaTarget(List<RelativeCoordinate> directions) {
        super(directions);
    }

    public ActionMetaTarget() {
    }

    public ActionMetaTarget(ActionMetaTarget actionMetaTarget) {
        super(actionMetaTarget);
    }

    public ActionMetaTarget(int numDirections, IRandomNumberGenerator rng) {
        super(numDirections, rng);
    }

    public int execute(Action action, RuleBasedPixel actor, Configuration configuration) {
        RelativeCoordinate direction = getCandidate(actor, configuration);
        if (direction == null) {
            return 0;
        }
        return action.execute(actor, direction, configuration);
    }

}
