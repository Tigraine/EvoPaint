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
        for (int i = 0; i < internalImage.length; i++) {
            Pixel pixie = world.getNotSynchronized(i);
            internalImage[i] =
                        pixie == null ?
                        world.getConfiguration().backgroundColor :
                        pixie.getPixelColor().getInteger();
        }
    }

    public Perception(BufferedImage image) {
        this.image = image;
        this.internalImage = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();

    }
}
