/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.interfaces.IAttribute;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author tam
 */
public class PixelPerceptionAttribute implements IAttribute {

    private BufferedImage perception;
    private int backgroundColor;
    private int type;
    private int zoom;

    @Override
    public String toString() {
        return "TODO: implement me in PixelPerceptionAttribute.java";
    }

    public void setPixel(int color, Point location) {
        for (int zy = 0; zy < zoom; zy++) {
            for (int zx = 0; zx < zoom; zx++) {
                this.perception.setRGB(zoom * location.x + zx, zoom * location.y + zy, color);
            }
        }
    }

    public void clear() {

        // clear bufferedImage (by setting it to transparent)
        Graphics2D g = (Graphics2D)this.perception.getGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0,0,this.perception.getWidth(),this.perception.getHeight());
        g.setComposite(AlphaComposite.SrcOver);
        g.dispose();
        //this.perception = new BufferedImage(this.perception.getWidth(),
        //        this.perception.getHeight(), this.type);
    }

    public BufferedImage getPerception() {
        return this.perception;
    }

    public int getType() {
        return type;
    }

    public int getZoom() {
        return zoom;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public PixelPerceptionAttribute(int width, int height, int type, int zoom) {
        this.type = type;
        this.zoom = zoom;
        if (type != BufferedImage.TYPE_INT_RGB &&
                type != BufferedImage.TYPE_INT_ARGB) {
            System.out.println("ERROR: Unsupported image type: " + type + "," +
                    "valid values are " + BufferedImage.TYPE_INT_RGB + " and " +
                    BufferedImage.TYPE_INT_ARGB);
            System.exit(1);
        }
        this.perception = new BufferedImage(width * zoom, height * zoom, type);
    }
}
