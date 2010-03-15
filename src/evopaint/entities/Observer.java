/*
 * static class to represent the organisms environment as well as the organisms
 * themselves in the flow of time
 */

package evopaint.entities;

import evopaint.Perception;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author tam
 */
public class Observer {
    private Perception perception;

    public void percieve(System system) {
        List<Pixel> pixels = system.getPixels();
        for (Pixel pixel : pixels) {
            this.perception.setPixel(pixel.getColorAttribute().getColor(), pixel.getLocation());
        }
    }

    public BufferedImage getPerception() {
        return this.perception.getImage();
    }

    public Observer(Perception perception) {
        this.perception = perception;
    }


}
