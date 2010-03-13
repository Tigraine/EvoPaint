package evopaint.util.objectrenderers;

import evopaint.entities.Pixel;
import evopaint.interfaces.IObjectRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 17:25:42
 * To change this template use File | Settings | File Templates.
 */
public class PixelRenderer implements IObjectRenderer {
    public String render(Object object) {
        Pixel pixel = (Pixel) object;

        return object.getClass().getSimpleName() + "(" + pixel.hashCode() + ")";
    }
}
