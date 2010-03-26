/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author tam
 */
public class RuleBasedPixel extends Pixel {
    private RuleSet ruleSet;

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void act(World world) {
        for (IRule rule : getRuleSet().getRules()) {
            if (rule.apply(this, world) && world.getConfiguration().oneActionPerPixel) {
                break;
            }
        }
    }

    public RuleBasedPixel(RuleBasedPixel pixel) {
        super(pixel);
        this.ruleSet = new RuleSet(pixel.ruleSet);
    }

    public RuleBasedPixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy, RuleSet ruleSet) {
        super(pixelColor, location, energy);
        this.ruleSet = ruleSet;
    }
}
