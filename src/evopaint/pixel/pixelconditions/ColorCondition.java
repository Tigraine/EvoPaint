/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.pixelconditions;

import evopaint.pixel.misc.ColorComparisonOperator;
import evopaint.pixel.AbstractPixelCondition;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author tam
 */
public class ColorCondition extends AbstractPixelCondition {

    private PixelColor desiredColor;
    private int minLikenessPercentage;
    ColorComparisonOperator comparisonOperator;

    @Override
    public String toString() {
        String ret = "color of ";
        ret += super.toString();
        ret += " has ";
        ret += comparisonOperator.toString();
        ret += " ";
        ret += minLikenessPercentage;
        ret += "% of ";
        ret += desiredColor;
        return ret;
    }

    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            if (comparisonOperator.compare(them.getPixelColor(), desiredColor, minLikenessPercentage) == false) {
                return false; // so this is what lazy evaluation looks like...
            }
        }
        return true;
    }

    public ColorCondition(List<RelativeCoordinate> directions, PixelColor desiredColor, int minLikenessPercentage, ColorComparisonOperator comparisonOperator) {
        super(directions);
        this.desiredColor = desiredColor;
        this.minLikenessPercentage = minLikenessPercentage;
        this.comparisonOperator = comparisonOperator;
    }
}
