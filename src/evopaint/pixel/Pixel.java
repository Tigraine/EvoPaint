/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.pixel;

import evopaint.World;
import evopaint.util.mapping.AbsoluteCoordinate;

/**
 *
 * @author tam
 */
public class Pixel extends AbstractAgent {

    private PixelColor color;
    private AbsoluteCoordinate location;

    public AbsoluteCoordinate getLocation() {
        return location;
    }

    public PixelColor getPixelColor() {
        return color;
    }
    
    public void act(World world) {
        for (Rule rule : rules) {
            if (rule.apply(this, world) && world.getConfiguration().oneActionPerPixel) {
                break;
            }
        }
    }

    public Pixel(int energy, PixelColor color, AbsoluteCoordinate location) {
        super(energy);
        this.color = color;
        this.location = location;
    }
}
