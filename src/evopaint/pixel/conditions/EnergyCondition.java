/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.conditions;

import evopaint.pixel.misc.NumberComparisonOperator;
import evopaint.pixel.AbstractPixelCondition;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author tam
 */
public class EnergyCondition extends AbstractPixelCondition {

    private NumberComparisonOperator comparisonOperator;
    private int energyValue;

    public NumberComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    @Override
    public String toString() {
        String ret = "energy of ";
        ret += super.toString();
        ret += " ";
        ret += comparisonOperator.toString();
        ret += " ";
        ret += energyValue;
        ret += "?";
        return ret;
    }

    @Override
    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            if (comparisonOperator.compare(them.getEnergy(), energyValue) == false) {
                return false; // so this is what lazy evaluation looks like...
            }
        }
        return true;
    }

    public EnergyCondition(List<RelativeCoordinate> directions, NumberComparisonOperator comparisonOperator, int energyValue) {
        super(directions);
        this.comparisonOperator = comparisonOperator;
        this.energyValue = energyValue;
    }
}
