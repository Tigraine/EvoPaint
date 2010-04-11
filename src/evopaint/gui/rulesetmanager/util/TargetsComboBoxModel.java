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

package evopaint.gui.rulesetmanager.util;

import evopaint.util.mapping.RelativeCoordinate;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class TargetsComboBoxModel extends DefaultComboBoxModel {

    public TargetsComboBoxModel() {
        addElement(RelativeCoordinate.CENTER);
        addElement(RelativeCoordinate.NORTH);
        addElement(RelativeCoordinate.NORTH_EAST);
        addElement(RelativeCoordinate.EAST);
        addElement(RelativeCoordinate.SOUTH_EAST);
        addElement(RelativeCoordinate.SOUTH);
        addElement(RelativeCoordinate.SOUTH_WEST);
        addElement(RelativeCoordinate.WEST);
        addElement(RelativeCoordinate.NORTH_WEST);
    }

}
