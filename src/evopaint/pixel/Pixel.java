/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.pixel;

import evopaint.World;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public abstract class Pixel {
    public static final int RULESET = 0;

    private PixelColor pixelColor;
    private AbsoluteCoordinate location;
    private int energy;

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

    public boolean isAlive() {
        return energy > 0;
    }

    public void reward(int energy) {
        // lazy way out for zero reward/cost actions
        if (energy == 0) {
            return;
        }

        // and a boundary check (0,MAX_INT) for the rest
        if (energy > 0) {
            if (Integer.MAX_VALUE - this.energy >= energy) {
                this.energy += energy;
            } else {
                this.energy = Integer.MAX_VALUE;
            }
        }
        else {
            if (this.energy + energy >= 0) {
                this.energy += energy;
            } else {
                this.energy = 0;
            }
        }
    }
    
    public abstract void act(World world);

    public Pixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy) {
        this.pixelColor = pixelColor;
        this.location = location;
        this.energy = energy;
    }

    public Pixel(Pixel pixel) {
        this.pixelColor = new PixelColor(pixel.pixelColor); // colors can be changed
        this.location = new AbsoluteCoordinate(pixel.location); // ACs can be moved
        this.energy = pixel.energy; // primitive
    }
}
