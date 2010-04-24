package evopaint.util.objectrenderers;

import evopaint.interfaces.IObjectRenderer;
import evopaint.pixel.rulebased.RuleBasedPixel;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 17:25:42
 * To change this template use File | Settings | File Templates.
 */
public class PixelRenderer implements IObjectRenderer {
    public String render(Object object) {
        RuleBasedPixel pixel = (RuleBasedPixel) object;

        return object.getClass().getSimpleName() + "(" + pixel.hashCode() + ")";
    }
}
