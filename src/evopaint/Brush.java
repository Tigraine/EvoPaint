/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author tam
 */
public class Brush {
    private Configuration configuration;
    public int size;

    public void paint(int x, int y) {
        
        for (int i = 0 - size / 2 ; i < (int)Math.ceil((double)size / 2); i++) {
            for (int j = 0 - size / 2; j < (int)Math.ceil((double)size / 2); j++) {
                PixelColor newColor = new PixelColor(configuration.paint.getColor());
                switch (configuration.paint.getMode()) {
                    case Paint.COLOR:
                        break;
                    case Paint.FAIRY_DUST:
                        newColor.setInteger(configuration.rng.nextPositiveInt());
                        break;
                    case Paint.USE_EXISTING:
                        Pixel pixie = configuration.world.get(x + j, y + i);
                        if (pixie == null) {
                            continue;
                        }
                        newColor.setHSB(pixie.getPixelColor().getHSB());
                        break;
                    default:
                        assert(false);
                }

                Pixel newPixel = null;
                switch (configuration.pixelType) {
                    case Pixel.RULESET:
                        newPixel = new RuleBasedPixel(newColor, new AbsoluteCoordinate(x + j, y + i, configuration.world), configuration.startingEnergy, configuration.paint.getRuleSet());
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
