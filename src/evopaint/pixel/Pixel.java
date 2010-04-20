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

package evopaint.pixel;

import evopaint.Configuration;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
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

    public void setLocation(AbsoluteCoordinate location) {
        this.location = location;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public void kill() {
            this.energy = 0;
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

    public void mixWith(Pixel them, float theirShare, IRandomNumberGenerator rng) {
        pixelColor = new PixelColor(pixelColor);
        pixelColor.mixWith(them.getPixelColor(), theirShare, new ColorDimensions(true, true, true));
    }


    public abstract void act(Configuration configuration);

    public Pixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy) {
        this.pixelColor = pixelColor;
        this.location = location;
        this.energy = energy;
    }

    public Pixel(Pixel pixel) {
        this.pixelColor = pixel.pixelColor;
        this.location = pixel.location;
        this.energy = pixel.energy;
    }

}
