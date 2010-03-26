/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.World;
import evopaint.pixel.rulebased.ObjectComparisonOperator;
import evopaint.pixel.rulebased.AbstractPixelCondition;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author tam
 */
public class EmptyCondition extends AbstractPixelCondition {
    private ObjectComparisonOperator comparisonOperator;

    @Override
    public String toString() {
        String ret = super.toString();
        ret += "empty?";
        return ret;
    }

    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            if (comparisonOperator.compare(them, null) == false) {
                return false; // so this is what lazy evaluation looks like...
            }
        }
        return true;
    }

    public EmptyCondition(List<RelativeCoordinate> directions, ObjectComparisonOperator comparisonOperator) {
        super(directions);
        this.comparisonOperator = comparisonOperator;
    }
}
