/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.Config;
import evopaint.Entity;
import evopaint.interfaces.IAttribute;
import evopaint.util.Log;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author tam
 */
public class PartsAttribute implements IAttribute {
    private List<Entity> parts;

    @Override
    public String toString() {
        String ret = this.getClass().getSimpleName();

        if (Config.logVerbosity == Log.Verbosity.VERBOSE) {
            ret += "(" + this.hashCode() + "){" + this.parts.size() + " parts}";
        }

        if (Config.logVerbosity == Log.Verbosity.VERBOSEVERBOSE) {
            ret += "(" + this.hashCode() + ")={";
            for (Entity part : this.parts) {
                ret += part + ",";
            }
            ret = ret.substring(0, ret.length() - 1);
            ret += "}";
        }

        return ret;
    }

    public List<Entity> getParts() {
        return parts;
    }

    public PartsAttribute(List<Entity> parts) {
        this.parts = parts;
    }
}
