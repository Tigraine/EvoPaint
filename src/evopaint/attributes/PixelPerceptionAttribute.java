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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author tam
 */
public class PixelPerceptionAttribute implements IAttribute {

    private Dimension dim;
    private BufferedImage perception;
    private int backgroundColor;

    @Override
    public String toString() {
        return "TODO: implement me in PixelPerceptionAttribute.java";
    }

    public synchronized void setPixel(int color, Point origin) {
        Point location = new Point(origin);
        //location.x += this.viewOffset.x;
        //location.y += this.viewOffset.y;
        //location = this.clamp(location);
        this.perception.setRGB(location.x, location.y, color);
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

    public synchronized BufferedImage getPerception() {
        return this.perception;
    }

    public int getType() {
        return this.perception.getType();
    }
/*
    public void translateViewOffset(int dx, int dy) {
        this.viewOffset.x += dx;
        this.viewOffset.y += dy;
    }
*/
    public int getBackgroundColor() {
        return backgroundColor;
    }
/*
    private Point clamp(Point p) {
        int sizeX = this.dim.width;
        int sizeY = this.dim.height;

        while (p.x < 0) {
            p.x += sizeX;
        }
        while (p.x >= sizeX) {
            p.x -= sizeX;
        }
        while (p.y < 0) {
            p.y += sizeY;
        }
        while (p.y >= sizeY) {
            p.y -= sizeY;
        }
        return p;
    }
*/
    public PixelPerceptionAttribute(int width, int height, int type) {
        if (type != BufferedImage.TYPE_INT_RGB &&
                type != BufferedImage.TYPE_INT_ARGB) {
            System.out.println("ERROR: Unsupported image type: " + type + "," +
                    "valid values are " + BufferedImage.TYPE_INT_RGB + " and " +
                    BufferedImage.TYPE_INT_ARGB);
            System.exit(1);
        }
        this.perception = new BufferedImage(width, height, type);
        //this.viewOffset = new Point(0,0);
        this.dim = new Dimension(width, height);
    }
}
