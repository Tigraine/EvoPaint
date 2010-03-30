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

    public ColorDimensions(boolean hue, boolean saturation, boolean brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }
}
