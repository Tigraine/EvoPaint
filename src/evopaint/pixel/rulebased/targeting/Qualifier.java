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
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.pixel.rulebased.interfaces.IParameterized;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class Qualifier implements INamed, IHTML, IParameterized, Serializable {

    public static final int COLOR_LIKENESS_COLOR = 0;
    public static final int COLOR_LIKENESS_MY_COLOR = 1;
    public static final int ENERGY = 2;
    public static final int EXISTENCE = 3;

    public abstract int getType();

    public abstract void mixWith(Qualifier theirQualifier, float theirShare, IRandomNumberGenerator rng);

    public String toHTML() {
        return getName();
    }

    public Map<String, String> addParametersString(Map<String, String> parametersMap) {
        return parametersMap;
    }

    public LinkedHashMap<String, JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        return parametersMap;
    }

    public Map<String, String> addParametersHTML(Map<String, String> parametersMap) {
        return parametersMap;
    }

    public abstract List<RelativeCoordinate> getCandidates(RuleBasedPixel actor, List<RelativeCoordinate> directions, Configuration configuration);

}