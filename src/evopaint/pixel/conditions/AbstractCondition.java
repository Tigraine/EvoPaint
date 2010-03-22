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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public abstract boolean isMet(Pixel us, RelativeCoordinate direction, World world);

    public AbstractCondition(String name) {
        this.name = name;
    }
}
