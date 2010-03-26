/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.pixel.rulebased.NumberComparisonOperator;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tam
 */
public class EnergyCondition extends AbstractCondition {
    private static final String NAME = "energy";

    private NumberComparisonOperator comparisonOperator;
    private int energyValue;

    public NumberComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(NumberComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(int energyValue) {
        this.energyValue = energyValue;
    }

    public String getName() {
        return NAME;
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

    public EnergyCondition() {
        super(new ArrayList<RelativeCoordinate>(9));
        this.comparisonOperator = NumberComparisonOperator.EQUAL;
        this.energyValue = 0;
    }
}
