/*
 * yadda
 */

package evopaint;

import evopaint.attributes.ColorAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.interfaces.IAttribute;
import evopaint.util.Log;
import java.util.IdentityHashMap;

/**
 *
 * @author tam
 */
public class Entity {
    protected IdentityHashMap<Class,IAttribute> attributes;

    @Override
    public String toString() {
        String ret = this.getClass().getSimpleName();
        if (Config.logVerbosity >= Log.Verbosity.VERBOSE) {
            ret += "(" + this.hashCode() + ")";
        }
        
        if (Config.logVerbosity >= Log.Verbosity.VERBOSEVERBOSE) {
            ret += "{";
            for (Class attributeType : this.attributes.keySet()) {
                ret += attributeType.getSimpleName();
                ret += "=";
                ret += this.attributes.get(attributeType);
                ret += ",";
            }
            ret = ret.substring(0, ret.length() - 1);
            ret += "}";
        }
        
        return ret;
    }

    public IdentityHashMap<Class, IAttribute> getAttributes() {
        return attributes;
    }

    public void setAttribute(Class attributeType, IAttribute attribute) {
        this.attributes.put(attributeType, attribute);
    }

    public IAttribute getAttribute(Class attributeType) {
        return this.attributes.get(attributeType);
    }

    public Entity(IdentityHashMap<Class,IAttribute> attributes) {
        this.attributes = attributes;
    }

    public Entity() {}
}
