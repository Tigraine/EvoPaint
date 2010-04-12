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

import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.SingleTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedActionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedConditionTarget;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JSpecifiedTarget extends JPanel {
    private JSingleTarget jTarget;

    public JSpecifiedTarget(ITarget target) {
        jTarget = new JSingleTarget((SingleTarget)target);
        add(jTarget);
    }

    public IConditionTarget createSpecifiedConditionTarget() {
        return new SpecifiedConditionTarget(jTarget.getDirection());
    }

    public IActionTarget createSpecifiedActionTarget() {
        return new SpecifiedActionTarget(jTarget.getDirection());
    }
}
