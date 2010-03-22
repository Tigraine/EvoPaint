/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.conditions;

import evopaint.World;
import evopaint.interfaces.ICondition;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class ConditionWrapper {
    private RelativeCoordinate direction;
    private ICondition condition;

    @Override
    public String toString() {
        return direction.toString().concat(" ").concat(condition.toString());
    }

    public RelativeCoordinate getDirection() {
        return direction;
    }
    
    public ICondition getCondition() {
        return condition;
    }

    public boolean isMet(Pixel us, World world) {
        return condition.isMet(us, direction, world);
    }

    public ConditionWrapper(RelativeCoordinate direction, ICondition condition) {
        this.direction = direction;
        this.condition = condition;
    }
}
