/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.util.mapping.ParallaxMap;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 *
 * @author tam
 */
public class Perception {

    private BufferedImage image;
    private int[] internalImage;

    //public void clear() {
        // clear bufferedImage (by setting it to transparent)
        //g.setComposite(AlphaComposite.Clear);
        //g.fillRect(0,0,this.perception.getWidth(),this.perception.getHeight());
        //g.setComposite(AlphaComposite.SrcOver);
        //this.graphicsContext.clearRect(0, 0, perception.getWidth(), perception.getHeight());
        //g.dispose();
    //}

    public BufferedImage getImage() {
        return this.image;
    }

    public int getType() {
        return this.image.getType();
    }

    public void percieve(ParallaxMap<Pixel> map) {
        int height = map.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Pixel pixie = map.get(x, y);
                internalImage[y * height + x] = pixie == null ? 0 : pixie.getColor().getInteger();
            }
        }
    }

    public Perception(int width, int height, int type) {
        if (type != BufferedImage.TYPE_INT_RGB &&
                type != BufferedImage.TYPE_INT_ARGB) {
            System.out.println("ERROR: Unsupported image type: " + type + "," +
                    "valid values are " + BufferedImage.TYPE_INT_RGB + " and " +
                    BufferedImage.TYPE_INT_ARGB);
            System.exit(1);
        }
        this.image = new BufferedImage(width, height, type);
        this.internalImage = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
    }
}
