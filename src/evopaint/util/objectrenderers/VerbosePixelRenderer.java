package evopaint.util.objectrenderers;

import evopaint.interfaces.IObjectRenderer;
import evopaint.pixel.rulebased.RuleBasedPixel;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 17:27:22
 * To change this template use File | Settings | File Templates.
 */
public class VerbosePixelRenderer implements IObjectRenderer {
    public String render(Object object) {
        RuleBasedPixel pixel = (RuleBasedPixel) object;
        String ret = object.getClass().getSimpleName();
        return ret;
    }
}
