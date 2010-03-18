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
public class EnergyRequirement extends AbstractRequirement implements IRequirement {
    public static final int GREATER_THAN = 0;
    public static final int LESS_THAN = 1;
    public static final int EQUAL = 2;
    public static final int GREATER_OR_EQUAL = 3;
    public static final int LESS_OR_EQUAL = 4;

    private int comparationOperation;
    private int threshold;

    public boolean isMet(Pixel pixie, ParallaxMap<Pixel> map) {
        switch (comparationOperation) {
            case GREATER_THAN: return pixie.getEnergy() > threshold;
            case LESS_THAN: return pixie.getEnergy() < threshold;
            case EQUAL: return pixie.getEnergy() == threshold;
            case GREATER_OR_EQUAL: return pixie.getEnergy() >= threshold;
            case LESS_OR_EQUAL: return pixie.getEnergy() <= threshold;
        }
        return false;
    }

    public EnergyRequirement(RelativeCoordinate direction, int threshold, int comparisonOperation) {
        super(direction);
        this.threshold = threshold;
        this.comparationOperation = comparisonOperation;
        String name = "has energy";
        switch (comparationOperation) {
            case GREATER_THAN: name = name.concat(" greater than " + threshold);
            break;
            case LESS_THAN: name = name.concat(" less than " + threshold);
            break;
            case EQUAL: name = name.concat(" equals " + threshold);
            break;
            case GREATER_OR_EQUAL: name = name.concat(" greater or equal " + threshold);
            break;
            case LESS_OR_EQUAL: name = name.concat(" lesser or equal " + threshold);
            break;
        }
        setName(name);
    }


}
