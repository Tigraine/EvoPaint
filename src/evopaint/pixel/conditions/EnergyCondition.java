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
public class EnergyCondition extends AbstractCondition implements ICondition {
    public static final int GREATER_THAN = 0;
    public static final int LESS_THAN = 1;
    public static final int EQUAL = 2;
    public static final int GREATER_OR_EQUAL = 3;
    public static final int LESS_OR_EQUAL = 4;

    private int comparationOperation;
    private int threshold;

    public boolean isMet(Pixel us, World world) {
        switch (comparationOperation) {
            case GREATER_THAN: return us.getEnergy() > threshold;
            case LESS_THAN: return us.getEnergy() < threshold;
            case EQUAL: return us.getEnergy() == threshold;
            case GREATER_OR_EQUAL: return us.getEnergy() >= threshold;
            case LESS_OR_EQUAL: return us.getEnergy() <= threshold;
        }
        return false;
    }

    public EnergyCondition(RelativeCoordinate direction, int threshold, int comparisonOperation) {
        super(direction);
        this.threshold = threshold;
        this.comparationOperation = comparisonOperation;
        String name = "has energy";
        switch (comparationOperation) {
            case GREATER_THAN: name = name.concat(" > " + threshold);
            break;
            case LESS_THAN: name = name.concat(" < " + threshold);
            break;
            case EQUAL: name = name.concat(" = " + threshold);
            break;
            case GREATER_OR_EQUAL: name = name.concat(" >= " + threshold);
            break;
            case LESS_OR_EQUAL: name = name.concat(" <= " + threshold);
            break;
        }
        setName(name);
    }


}
