/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.NumberComparisonOperator;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tam
 */
public class ColorLikenessCondition extends AbstractCondition {
    private static final String NAME = "color likeness";

    private PixelColor comparedColor;
    private int dimensions;
    private int compareToLikenessPercentage;
    private NumberComparisonOperator comparisonOperator;

    public int getCompareToLikenessPercentage() {
        return compareToLikenessPercentage;
    }

    public void setCompareToLikenessPercentage(int compareToLikenessPercentage) {
        this.compareToLikenessPercentage = compareToLikenessPercentage;
    }

    public PixelColor getComparedColor() {
        return comparedColor;
    }

    public void setComparedColor(PixelColor comparedColor) {
        this.comparedColor = comparedColor;
    }

    public NumberComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(NumberComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        String ret = "color-likeness of ";
        ret += super.toString();
        ret += " compared to ";
        ret += comparedColor;
        ret += " in dimensions [";
        switch(dimensions) {
            case PixelColor.HSB: ret += "H,S,B";
                break;
            case PixelColor.H: ret += "H";
                break;
            case PixelColor.S: ret += "S";
                break;
            case PixelColor.B: ret += "B";
                break;
            case PixelColor.HS: ret += "H,S";
                break;
            case PixelColor.HB: ret += "H,B";
                break;
            case PixelColor.SB: ret += "S,B";
                break;
            default: assert(false);
        }
        ret += "] is ";
        ret += comparisonOperator;
        ret += " ";
        ret += compareToLikenessPercentage;
        ret += "%";
        
        return ret;
    }

    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            double distance = them.getPixelColor().distanceTo(comparedColor, dimensions);
            //System.out.println("distance: " + distance);
            int likenessPercentage = (int)((1 - distance) * 100);
            return comparisonOperator.compare(likenessPercentage, compareToLikenessPercentage);
        }
        return true;
    }

    public ColorLikenessCondition(List<RelativeCoordinate> directions, NumberComparisonOperator comparisonOperator, int compareToLikenessPercentage, int dimensions, PixelColor comparedColor) {
        super(directions);
        this.comparisonOperator = comparisonOperator;
        this.comparedColor = comparedColor;
        this.compareToLikenessPercentage = compareToLikenessPercentage;
        this.dimensions = dimensions;
    }

    public ColorLikenessCondition() {
        super(new ArrayList<RelativeCoordinate>(9));
        this.comparisonOperator = NumberComparisonOperator.EQUAL;
        this.comparedColor = new PixelColor(0, 0, 0);
        this.compareToLikenessPercentage = 0;
        this.dimensions = PixelColor.HSB;
    }
}
