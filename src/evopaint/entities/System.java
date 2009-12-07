/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.entities;

import evopaint.Entity;
import evopaint.attributes.PartsAttribute;
import evopaint.attributes.RelationsAttribute;
import evopaint.interfaces.IAttribute;
import evopaint.Relation;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;

/**
 *
 * @author tam
 */
public class System extends Entity {

    public void add(Entity entity) {
        PartsAttribute pa = (PartsAttribute) this.attributes.get(PartsAttribute.class);
        assert (pa != null);
        Collection parts = pa.getParts();
        parts.add(entity);
    }

    public void add(Relation relation) {
        RelationsAttribute ra = (RelationsAttribute) this.attributes.get(RelationsAttribute.class);
        assert (ra != null);
        List relations = ra.getRelations();
        relations.add(relation);
    }

    public void remove(Entity entity) {
        PartsAttribute pa = (PartsAttribute) this.attributes.get(PartsAttribute.class);
        assert (pa != null);
        Collection parts = pa.getParts();
        parts.remove(entity);
    }

    public void remove(Relation relation) {
        RelationsAttribute ra = (RelationsAttribute) this.attributes.get(RelationsAttribute.class);
        assert (ra != null);
        List relations = ra.getRelations();
        relations.remove(relation);
    }

    public System(IdentityHashMap<Class, IAttribute> attributes, PartsAttribute pa, RelationsAttribute ra) {
        super(attributes);
        this.attributes.put(PartsAttribute.class, pa);
        this.attributes.put(RelationsAttribute.class, ra);
    }

    public System() {}
}
