/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.conditions;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.interfaces.ICondition;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public abstract class AbstractCondition implements ICondition {

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

    public abstract boolean isMet(Pixel us, World world);

    public AbstractCondition(RelativeCoordinate direction) {
        this.name = "Unnamed Condition";
        this.direction = direction;
    }

    public AbstractCondition(String name, RelativeCoordinate direction) {
        this.name = name;
        this.direction = direction;
    }
}
