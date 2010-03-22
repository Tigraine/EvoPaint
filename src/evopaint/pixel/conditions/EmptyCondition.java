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
public class EmptyCondition extends AbstractCondition implements ICondition {
    
    public boolean isMet(Pixel us, RelativeCoordinate direction, World world) {
        return world.get(us.getLocation(), direction) == null;
    }

    public EmptyCondition(String name) {
        super(name);
    }
}
