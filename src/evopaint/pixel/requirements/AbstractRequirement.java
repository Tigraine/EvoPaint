/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.requirements;

import evopaint.pixel.Pixel;
import evopaint.interfaces.IRequirement;
import evopaint.util.mapping.ParallaxMap;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public abstract class AbstractRequirement implements IRequirement {

    private String name;
    protected RelativeCoordinate direction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return direction + " " + name;
    }
    
    public RelativeCoordinate getDirection() {
        return direction;
    }

    public void setDirection(RelativeCoordinate direction) {
        this.direction = direction;
    }

    public abstract boolean isMet(Pixel pixel, ParallaxMap<Pixel> map);

    public AbstractRequirement(RelativeCoordinate direction) {
        this.name = "Unnamed Requirement";
        this.direction = direction;
    }

    public AbstractRequirement(String name, RelativeCoordinate direction) {
        this.name = name;
        this.direction = direction;
    }
}
