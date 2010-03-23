/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author tam
 */
public class Brush {

    private int brushSize;
    private PixelColor pixelColor;
    private Pixel pixel;

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public Pixel createPixelAt(AbsoluteCoordinate absoluteCoordinate) {
        return new Pixel(new PixelColor(pixel.getPixelColor()), absoluteCoordinate,
                pixel.getEnergy(), pixel.getRuleSet(), pixel.getState(), pixel.getPossibleStates());
    }

    public Brush(int brushSize, Pixel pixel) {
        this.brushSize = brushSize;
        this.pixel = pixel;
    }
}
