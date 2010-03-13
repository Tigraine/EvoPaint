package evopaint.util.objectrenderers;

import evopaint.entities.Pixel;
import evopaint.interfaces.IObjectRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 17:27:22
 * To change this template use File | Settings | File Templates.
 */
public class VerbosePixelRenderer implements IObjectRenderer {
    public String render(Object object) {
        Pixel pixel = (Pixel) object;
        String ret = object.getClass().getSimpleName() + "{";
            for (Class attributeType : pixel.getAttributes().keySet()) {
                ret += attributeType.getSimpleName();
                ret += "=";
                ret += pixel.getAttributes().get(attributeType);
                ret += ",";
            }
            ret = ret.substring(0, ret.length() - 1);
            ret += "}";
        return ret;
    }
}
