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

import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.interfaces.IHTML;
import java.io.Serializable;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ColorDimensions implements IHTML, Serializable {
    public boolean hue;
    public boolean saturation;
    public boolean brightness;

    @Override
    public String toString() {
        String ret = "[";
        ret += hue ? "H" + (saturation || brightness ? ", " : "") : "";
        ret += saturation ? "S" + (brightness ? ", " : "") : "";
        ret += brightness ? "B" : "";
        ret += "]";
        return ret;
    }

    public String toHTML() {
        String ret = "[";
        ret += hue ? "H" + (saturation || brightness ? ", " : "") : "";
        ret += saturation ? "S" + (brightness ? ", " : "") : "";
        ret += brightness ? "B" : "";
        ret += "]";
        return ret;
    }
    
    public void mixWith(ColorDimensions theirColorDimensions, float theirShare, IRandomNumberGenerator rng) {
        if (rng.nextFloat() < theirShare) {
            hue = theirColorDimensions.hue;
        }
        if (rng.nextFloat() < theirShare) {
            saturation = theirColorDimensions.saturation;
        }
        if (rng.nextFloat() < theirShare) {
            brightness = theirColorDimensions.brightness;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColorDimensions other = (ColorDimensions) obj;
        if (this.hue != other.hue) {
            return false;
        }
        if (this.saturation != other.saturation) {
            return false;
        }
        if (this.brightness != other.brightness) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.hue ? 1 : 0);
        hash = 29 * hash + (this.saturation ? 1 : 0);
        hash = 29 * hash + (this.brightness ? 1 : 0);
        return hash;
    }

    public ColorDimensions(boolean hue, boolean saturation, boolean brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public ColorDimensions(ColorDimensions colorDimensions) {
        this.hue = colorDimensions.hue;
        this.saturation = colorDimensions.saturation;
        this.brightness = colorDimensions.brightness;
    }
    
}
