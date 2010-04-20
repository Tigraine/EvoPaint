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
import evopaint.util.mapping.AbsoluteCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class RuleBasedPixel extends Pixel {
    private List<Rule> rules;

    public RuleSet createRuleSet() {
        return new RuleSet(rules);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void act(Configuration configuration) {
        if (rules == null) {
            return;
        }
        for (Rule rule : rules) {
            if (rule.apply(this, configuration)) {
                break;
            }
        }
    }

    public RuleBasedPixel(RuleBasedPixel pixel) {
        super(pixel);
        this.rules = new ArrayList(pixel.rules);
    }

    public RuleBasedPixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy, List<Rule> rules) {
        super(pixelColor, location, energy);
        this.rules = rules;
    }
}
