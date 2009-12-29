/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.interfaces.IAttribute;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author tam
 */
public class PixelPerceptionAttribute implements IAttribute {

    private BufferedImage perception;
    private int backgroundColor;
    private int[] internalPerception;

    @Override
    public String toString() {
        return "TODO: implement me in PixelPerceptionAttribute.java";
    }

    public synchronized void setPixel(int color, Point origin) {
        this.internalPerception[origin.y * this.perception.getWidth() + origin.x] = color;
    }

    public void clear() {
        // clear bufferedImage (by setting it to transparent)
        Graphics2D g = (Graphics2D)this.perception.getGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0,0,this.perception.getWidth(),this.perception.getHeight());
        g.setComposite(AlphaComposite.SrcOver);
        g.dispose();
    }

    public synchronized BufferedImage getPerception() {
        return this.perception;
    }

    public int getType() {
        return this.perception.getType();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public PixelPerceptionAttribute(int width, int height, int type) {
        if (type != BufferedImage.TYPE_INT_RGB &&
                type != BufferedImage.TYPE_INT_ARGB) {
            System.out.println("ERROR: Unsupported image type: " + type + "," +
                    "valid values are " + BufferedImage.TYPE_INT_RGB + " and " +
                    BufferedImage.TYPE_INT_ARGB);
            System.exit(1);
        }
        this.perception = new BufferedImage(width, height, type);
        this.internalPerception = ((DataBufferInt)this.perception.getRaster().getDataBuffer()).getData();
    }
}
