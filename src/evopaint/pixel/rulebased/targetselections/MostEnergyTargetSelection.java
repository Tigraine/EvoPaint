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
public class MostEnergyTargetSelection extends AbstractTargetSelection {

    public MostEnergyTargetSelection(List<RelativeCoordinate> directions) {
        super("with the most energy", directions);
    }

    public MostEnergyTargetSelection() {
        super("with the most energy");
    }

    public List<RelativeCoordinate> getAllSelectedDirections(Pixel origin, Configuration configuration) {
        int maxEnergy = 0;
        List<RelativeCoordinate> ret = new ArrayList(1);
        for (RelativeCoordinate direction : directions) {
            Pixel target = configuration.world.get(origin.getLocation(), direction);
            if (target == null) {
                continue;
            }
            if (ret.size() > 0 && target.getEnergy() > maxEnergy) {
                maxEnergy = target.getEnergy();
                ret.clear();
            }
            ret.add(direction);
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
        return "with the most energy";
    }

    @Override
    protected String toStringCallback() {
        return "with the most energy";
    }
}
