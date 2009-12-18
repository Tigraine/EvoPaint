package evopaint.util.objectrenderers;

import evopaint.Relation;
import evopaint.interfaces.IObjectRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 17:39:45
 * To change this template use File | Settings | File Templates.
 */
public class RelationRenderer implements IObjectRenderer {
    public String render(Object object) {
        Relation relation = (Relation) object;
        return "(" + relation.hashCode() + ")";
    }
}
