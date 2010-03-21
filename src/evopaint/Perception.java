/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.World;
import evopaint.pixel.Pixel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author tam
 */
public class Perception {
    private BufferedImage image;
    private int[] internalImage;

    public BufferedImage getImage() {
        return image;
    }

    public void createImage(World world) {
        int height = world.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                Pixel pixie = world.get(x, y);
                internalImage[y * height + x] =
                        pixie == null ?
                        world.getConfiguration().backgroundColor :
                        pixie.getPixelColor().getInteger();
            }
        }
    }

    public Perception(BufferedImage image) {
        this.image = image;
        this.internalImage = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();

    }
}
