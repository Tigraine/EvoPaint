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

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class RuleBasedPixel extends Pixel {
    private RuleSet ruleSet;

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void act(Configuration configuration) {
        if (ruleSet == null) {
            return;
        }
        for (IRule rule : ruleSet.getRules()) {
            if (rule.apply(this, configuration)) {
                break;
            }
        }
    }

    public RuleBasedPixel(RuleBasedPixel pixel, boolean copyRuleSet) {
        super(pixel);
        if (copyRuleSet) {
            this.ruleSet = new RuleSet(pixel.ruleSet);
        } else {
            this.ruleSet = pixel.ruleSet;
        }
    }

    public RuleBasedPixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy, RuleSet ruleSet) {
        super(pixelColor, location, energy);
        this.ruleSet = ruleSet;
    }
}
