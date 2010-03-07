package evopaint.util.objectrenderers;

import evopaint.interfaces.IObjectRenderer;

import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 10.01.2010
 * Time: 14:03:55
 * To change this template use File | Settings | File Templates.
 */
public class AffineTransformRenderer implements IObjectRenderer {
    public String render(Object object) {
        AffineTransform transform = (AffineTransform) object;
        return "Scale: " + transform.getScaleX() + " Translate: " + transform.getTranslateX();
    }
}
