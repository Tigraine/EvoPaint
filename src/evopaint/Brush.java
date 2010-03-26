/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.State;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author tam
 */
public class Brush {
    public static final int COLOR = 0;
    public static final int FAIRY_DUST = 1;
    public static final int USE_EXISTING = 2;

    private Configuration configuration;

    private int brushSize;
    private int mode;
    
    private PixelColor color;
    // we don't know the location yet
    // private AbsoluteCoordinate location;
    private int energy;
    private RuleSet ruleSet;

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int radius) {
        this.brushSize = radius;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public PixelColor getColor() {
        return color;
    }

    public void setColor(PixelColor color) {
        this.color = color;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void paint(int x, int y) {
        
        for (int i = 0 - brushSize / 2 ; i < (int)Math.ceil((double)brushSize / 2); i++) {
            for (int j = 0 - brushSize / 2; j < (int)Math.ceil((double)brushSize / 2); j++) {
               
                switch (mode) {
                    case COLOR:
                        break;
                    case FAIRY_DUST:
                        color.setInteger(configuration.rng.nextPositiveInt(), configuration.rng);
                        break;
                    case USE_EXISTING:
                        Pixel pixie = configuration.world.get(x + j, y + i);
                        if (pixie == null) {
                            continue;
                        }
                        color.setHSB(pixie.getPixelColor().getHSB());
                        break;
                    default:
                        assert(false);
                }

                Pixel newPixel = null;
                switch (configuration.pixelType) {
                    case Pixel.RULESET:
                        newPixel = new RuleBasedPixel(new PixelColor(color), new AbsoluteCoordinate(x + j, y + i, configuration.world), energy, ruleSet);
                        break;
                    default: assert(false);
                }
                configuration.world.set(newPixel);
            }
        }
    }

    public Brush(Configuration configuration) {
        this.configuration = configuration;
        this.energy = configuration.startingEnergy;
        this.brushSize = 10;
        this.mode = COLOR;
        this.color = new PixelColor(0xFF0000, configuration.rng);
        this.ruleSet = configuration.createDefaultRuleSet();
    }
}
