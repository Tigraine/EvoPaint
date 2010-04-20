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

package evopaint.gui.rulesetmanager;

import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.ActionSingleTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.MetaTarget;
import evopaint.pixel.rulebased.targeting.SingleTarget;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JActionTargetPanel extends JPanel {
    private JTarget jTarget;

    public JActionTargetPanel(ITarget target) {
        jTarget = new JTarget(target);
        add(jTarget);
    }

    public ITarget createTarget() {
        
        ITarget target = jTarget.createTarget();

        if (target == null) {
            return new ActionSingleTarget();
        }

        if (target instanceof MetaTarget) {
            return new ActionMetaTarget(((MetaTarget)target).getDirections());
        }

        return new ActionSingleTarget(((SingleTarget)target).getDirection());
    }

}
