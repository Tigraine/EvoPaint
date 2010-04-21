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
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import javax.swing.JComponent;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class CopyAction extends Action {

    public CopyAction(int energyChange, ActionMetaTarget target) {
        super(energyChange, target);
    }

    public int getType() {
        return Action.COPY;
    }

    public CopyAction() {
    }

    public CopyAction(CopyAction copyAction) {
        super(copyAction);
    }

    public String getName() {
        return "copy";
    }

    public int execute(RuleBasedPixel actor, RelativeCoordinate direction, Configuration configuration) {
        RuleBasedPixel target = configuration.world.get(actor.getLocation(), direction);
        if (target != null) {
            return 0;
        }

        RuleBasedPixel newPixel = new RuleBasedPixel(actor);
        newPixel.setLocation(new AbsoluteCoordinate(actor.getLocation(), direction, configuration.world));
        //newPixel.setEnergy(actor.getEnergy() + energyChange);
        configuration.world.set(newPixel);

        if (configuration.rng.nextDouble() <= configuration.mutationRate) {
            newPixel.mutate(configuration);
        }

        return energyChange;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        return parametersMap;
    }
    
}
