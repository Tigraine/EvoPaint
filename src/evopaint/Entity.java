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

    public IdentityHashMap<Class, IAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(IdentityHashMap<Class, IAttribute> attributes) {
        this.attributes = attributes;
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
