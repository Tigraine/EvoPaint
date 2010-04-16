/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.pixel;

import evopaint.Configuration;
import evopaint.util.mapping.AbsoluteCoordinate;

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

    public void setPixelColor(float [] hsb) {
        this.pixelColor.setHSB(hsb);
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

    public void changeEnergy(int energy) {
        // lazy way out for non energy changing actions
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
    
    public abstract void act(Configuration configuration);

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
