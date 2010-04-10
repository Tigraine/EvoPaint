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
import evopaint.pixel.rulebased.Action;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class SpecifiedActionTarget
        extends SpecifiedTarget implements IActionTarget {

    public SpecifiedActionTarget(List<RelativeCoordinate> directions) {
        super(directions);
    }

    public SpecifiedActionTarget() {
    }

    public int execute(Action action, Pixel actor, Configuration configuration) {
        int totalCost = 0;
        for (RelativeCoordinate direction : directions) {
            totalCost += action.execute(actor, direction, configuration);
        }
        return totalCost;
    }
    
}
