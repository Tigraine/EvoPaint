/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author tam
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
                        Pixel pixie = configuration.world.get(x, y);
                        if (pixie == null) {
                            continue;
                        }
                        newColor.setHSB(pixie.getPixelColor().getHSB());
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
                        Pixel pixie = configuration.world.get(x, y);
                        if (pixie == null) {
                            ruleSet = null;
                        } else {
                            ruleSet = ((RuleBasedPixel)pixie).getRuleSet();
                        }
                        break;
                    default:
                        assert(false);
                }

                Pixel newPixel = null;
                switch (configuration.pixelType) {
                    case Pixel.RULESET:
                        newPixel = new RuleBasedPixel(newColor, new AbsoluteCoordinate(x, y, configuration.world), configuration.startingEnergy, ruleSet);
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
