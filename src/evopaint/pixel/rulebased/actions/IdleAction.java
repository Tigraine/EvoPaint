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

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class IdleAction extends Action {

    public IdleAction(int energyChange) {
        super(energyChange, null);
    }

    public IdleAction() {
    }

    public String getName() {
        return "idle";
    }

    @Override // doing nothing does not need target checking
    public int execute(Pixel actor, Configuration configuration) {
        return energyChange;
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        // MEEP MEEEEEEP, I CAN HAS NEVAR BE CALLED!
        assert (false);
        return energyChange;
    }

}
