/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.Relation;
import evopaint.interfaces.IAttribute;
import java.util.List;

/**
 *
 * @author tam
 */
public class RelationsAttribute implements IAttribute {
    private List<Relation> relations;

    @Override
    public String toString() {
        return "TODO: implement me in RelationsAttribute.java";
    }

    public List<Relation> getRelations() {
        return this.relations;
    }

    public RelationsAttribute(List<Relation> relations) {
        this.relations = relations;
    }
}
