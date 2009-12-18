package evopaint.util.objectrenderers;

import evopaint.Entity;
import evopaint.interfaces.IObjectRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 17:25:42
 * To change this template use File | Settings | File Templates.
 */
public class EntityRenderer implements IObjectRenderer {
    public String render(Object object) {
        Entity entity = (Entity) object;

        return object.getClass().getSimpleName() + "(" + entity.hashCode() + ")";
    }
}
