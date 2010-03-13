/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.attributes;

import evopaint.entities.Pixel;
import evopaint.interfaces.IAttribute;
import java.util.List;

/**
 *
 * @author tam
 */
public class ColorPullAttribute implements IAttribute {
   // private boolean chooseNearest
    public static final int BY_DIRECTION = 0;
    public static final int BY_COLOR = 1;
    public static final int DIRECTION = 0;



    @Override
    public String toString() {
        String ret = this.getClass().getSimpleName();

        /*if (Config.logVerbosity == Log.Verbosity.VERBOSE) {
            ret += "(" + this.hashCode() + "){" + this.parts.size() + " parts}";
        }

        if (Config.logVerbosity == Log.Verbosity.VERBOSEVERBOSE) {
            ret += "(" + this.hashCode() + ")={";
            for (Entity part : this.parts) {
                ret += part + ",";
            }
            ret = ret.substring(0, ret.length() - 1);
            ret += "}";
        } */

        return ret;
    }

    public ColorPullAttribute(List<Pixel> parts) {
        //this.parts = parts;
    }
}
