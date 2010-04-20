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

package evopaint;

import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.mapping.AbsoluteCoordinate;
import java.util.ArrayList;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Brush {
    private Configuration configuration;
    public int size;

    public void paint(int xLoc, int yLoc) {
        int yBegin = yLoc - size / 2;
        if (yBegin < 0) {
        //    yBegin = 0;
        }
        int yEnd = yLoc + (int)Math.ceil((double)size / 2);
        if (yEnd > configuration.dimension.height) {
       //     yEnd = configuration.dimension.height;
        }

        int xBegin = xLoc - size / 2;
        if (xBegin < 0) {
       //     xBegin = 0;
        }
        int xEnd = xLoc + (int)Math.ceil((double)size / 2);
        if (xEnd > configuration.dimension.width) {
       //     xEnd = configuration.dimension.width;
        }

        for (int y = yBegin; y < yEnd; y++) {
            for (int x = xBegin; x < xEnd; x++) {

                PixelColor newColor = new PixelColor(configuration.paint.getCurrentColor());
                switch (configuration.paint.getCurrentColorMode()) {
                    case Paint.COLOR:
                        break;
                    case Paint.FAIRY_DUST:
                        newColor.setInteger(configuration.rng.nextPositiveInt());
                        break;
                    case Paint.EXISTING_COLOR:
                        RuleBasedPixel pixie = configuration.world.get(x, y);
                        if (pixie == null) {
                            continue;
                        }
                        newColor.setColor(pixie.getPixelColor());
                        break;
                    default:
                        assert(false);
                }

                RuleSet ruleSet = configuration.paint.getCurrentRuleSet();
                switch (configuration.paint.getCurrentRuleSetMode()) {
                    case Paint.RULE_SET:
                        break;
                    case Paint.NO_RULE_SET:
                        ruleSet = null;
                        break;
                    case Paint.EXISTING_RULE_SET:
                        RuleBasedPixel pixie = configuration.world.get(x, y);
                        if (pixie == null) {
                            ruleSet = null;
                        } else {
                            ruleSet = pixie.createRuleSet();
                        }
                        break;
                    default:
                        assert(false);
                }

                RuleBasedPixel newPixel = null;
                switch (configuration.pixelType) {
                    case RuleBasedPixel.RULESET:
                        newPixel = new RuleBasedPixel(newColor, new AbsoluteCoordinate(x, y, configuration.world), configuration.startingEnergy, ruleSet != null ? ruleSet.getRules() : new ArrayList());
                        break;
                    default: assert(false);
                }
                configuration.world.set(newPixel);
            }
        }
    }

    public Brush(Configuration configuration) {
        this.configuration = configuration;
        this.size = 10;
    }
}
