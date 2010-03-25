/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.pixel;

import evopaint.World;
import evopaint.pixel.interfaces.IRule;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author tam
 */
public class Pixel {

    private PixelColor pixelColor;
    private AbsoluteCoordinate location;
    private int energy;
    private RuleSet ruleSet;
    private State state;

    public PixelColor getPixelColor() {
        return pixelColor;
    }

    public void setPixelColor(PixelColor pixelColor) {
        this.pixelColor = pixelColor;
    }

    public AbsoluteCoordinate getLocation() {
        return location;
    }

    public int getEnergy() {
        return energy;
    }

    public void setLocation(AbsoluteCoordinate location) {
        this.location = location;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public void reward(int energy) {
        this.energy += energy;
    }
    
    public void act(World world) {
        for (IRule rule : getRuleSet().getRules()) {
            if (rule.apply(this, world) && world.getConfiguration().oneActionPerPixel) {
                break;
            }
        }
    }

    public Pixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy, RuleSet ruleSet) {
        this.pixelColor = pixelColor;
        this.location = location;
        this.energy = energy;
        this.ruleSet = ruleSet;
        this.state = ruleSet.getInitialState();
    }

    public Pixel(Pixel pixel) {
        this.pixelColor = new PixelColor(pixel.pixelColor); // needs own color attribute
        this.location = pixel.location; // absolute coordinates stay the same (or change when moving)
        this.energy = pixel.energy; // primitive
        this.ruleSet = pixel.ruleSet; // if we mix rulesets later, we will serialize them anyway
        this.state = pixel.state; // states are the same objects. if I change the name of a state it shall be changed everywhere
    }
}
