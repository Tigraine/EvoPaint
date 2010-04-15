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
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import javax.swing.JComponent;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class MoveAction extends Action {

    public MoveAction(int energyChange, ActionMetaTarget target) {
        super(energyChange, target);
    }

    public MoveAction() {
    }

    public String getName() {
        return "moveTo";
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        Pixel target = configuration.world.get(actor.getLocation(), direction);
        if (target != null) {
            return 0;
        }
        configuration.world.remove(actor.getLocation());
        actor.getLocation().move(direction, configuration.world);
        configuration.world.set(actor);

        return energyChange;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        return parametersMap;
    }
    
}
