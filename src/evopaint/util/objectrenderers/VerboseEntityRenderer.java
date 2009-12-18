package evopaint.util.objectrenderers;

import evopaint.Entity;
import evopaint.interfaces.IObjectRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 17:27:22
 * To change this template use File | Settings | File Templates.
 */
public class VerboseEntityRenderer implements IObjectRenderer {
    public String render(Object object) {
        Entity entity = (Entity) object;
        String ret = object.getClass().getSimpleName() + "{";
            for (Class attributeType : entity.getAttributes().keySet()) {
                ret += attributeType.getSimpleName();
                ret += "=";
                ret += entity.getAttributes().get(attributeType);
                ret += ",";
            }
            ret = ret.substring(0, ret.length() - 1);
            ret += "}";
        return ret;
    }
}
