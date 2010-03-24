/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.pixel;

import evopaint.World;
import evopaint.pixel.interfaces.IRule;
import evopaint.util.mapping.AbsoluteCoordinate;
import java.util.List;

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
    private List<State> possibleStates;

    public PixelColor getPixelColor() {
        return pixelColor;
    }

    public AbsoluteCoordinate getLocation() {
        return location;
    }

    public int getEnergy() {
        return energy;
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

    public List<State> getPossibleStates() {
        return possibleStates;
    }

    public void setPossibleStates(List<State> possibleStates) {
        this.possibleStates = possibleStates;
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

    public Pixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy, RuleSet ruleSet, State state, List<State> possibleStates) {
        this.pixelColor = pixelColor;
        this.location = location;
        this.energy = energy;
        this.ruleSet = ruleSet;
        this.state = state;
        this.possibleStates = possibleStates;
    }

    public Pixel(Pixel pixel) {
        this.pixelColor = new PixelColor(pixel.pixelColor); // needs own color attribute
        this.location = pixel.location; // absolute coordinates stay the same (or change when moving)
        this.energy = pixel.energy; // primitive
        this.ruleSet = pixel.ruleSet; // if we mix rulesets later, we will serialize them anyway
        this.state = new State(pixel.state);
        this.possibleStates = pixel.possibleStates; // will have to be serialized together with the ruleset
    }
}
