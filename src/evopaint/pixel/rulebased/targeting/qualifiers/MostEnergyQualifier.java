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

package evopaint.pixel.rulebased.targeting.qualifiers;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.targeting.IQualifier;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class MostEnergyQualifier implements IQualifier {

    private static MostEnergyQualifier instance;

    public String getName() {
        return "has the most energy";
    }

    public List<RelativeCoordinate> getCandidates(Pixel origin, List<RelativeCoordinate> directions, Configuration configuration) {
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

    private MostEnergyQualifier() {
    }

    public static MostEnergyQualifier getInstance() {
        if (instance == null) {
            instance = new MostEnergyQualifier();
        }
        return instance;
    }

}
