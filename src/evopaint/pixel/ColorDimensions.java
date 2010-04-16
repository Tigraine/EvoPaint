/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.pixel.rulebased.interfaces.IHTML;
import java.io.Serializable;

/**
 *
 * @author tam
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
}
