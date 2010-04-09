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

package evopaint.pixel.rulebased.targetselections;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.AbstractTargetSelection;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ExistentTargetSelection extends AbstractTargetSelection {

    public ExistentTargetSelection(List<RelativeCoordinate> directions) {
        super("that exist", directions);
    }

    public ExistentTargetSelection() {
        super("that exist");
    }

    public List<RelativeCoordinate> getAllSelectedDirections(Pixel origin, Configuration configuration) {
        List<RelativeCoordinate> ret = new ArrayList(1);
        for (RelativeCoordinate direction : directions) {
            Pixel target = configuration.world.get(origin.getLocation(), direction);
            if (target != null) {
                ret.add(direction);
            }
        }
        return ret;
    }

    public RelativeCoordinate getOneSelectedDirection(Pixel origin, Configuration configuration) {
        List<RelativeCoordinate> selectedDirections = getAllSelectedDirections(origin, configuration);
        if (selectedDirections.size() == 0) {
            return null;
        }
        return selectedDirections.get(configuration.rng.nextPositiveInt(selectedDirections.size()));
    }

    @Override
    protected String toHTMLCallback() {
        return "that exist";
    }

    @Override
    protected String toStringCallback() {
        return "that exist";
    }
}
