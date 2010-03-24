/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.conditions;

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
    public static final int LIKENESS_AT_LEAST= 0;
    public static final int LIKENESS_LESS_THAN = 1;

    public static final int HUE_LIKENESS_AT_LEAST= 2;
    public static final int HUE_LIKENESS_LESS_THAN = 3;
    public static final int SATURATION_LIKENESS_AT_LEAST= 4;
    public static final int SATURATION_LIKENESS_LESS_THAN = 5;
    public static final int BRIGHTNESS_LIKENESS_AT_LEAST= 6;
    public static final int BRIGHTNESS_LIKENESS_LESS_THAN = 7;

    private PixelColor desiredColor;
    private int minLikenessPercentage;
    int comparison;

    @Override
    public String toString() {
        String ret = "color of ";
        ret += super.toString();
        ret += " is ";
        switch (comparison) {
            case LIKENESS_AT_LEAST: ret += "at least"; break;
            case LIKENESS_LESS_THAN: ret += "less than"; break;
            case HUE_LIKENESS_AT_LEAST: ret += "at least"; break;
            case HUE_LIKENESS_LESS_THAN: ret += "less than"; break;
            case SATURATION_LIKENESS_AT_LEAST: ret += "at least"; break;
            case SATURATION_LIKENESS_LESS_THAN: ret += "less than"; break;
            case BRIGHTNESS_LIKENESS_AT_LEAST: ret += "at least"; break;
            case BRIGHTNESS_LIKENESS_LESS_THAN: ret += "less than"; break;
        }
        ret += " ";
        ret += minLikenessPercentage;
        ret += "% like ";
        ret += desiredColor;
        return ret;
    }

    public boolean isMet(Pixel us, World world) {
        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            int percentage = (int)PixelColor.likeness(them.getPixelColor(), desiredColor, comparison) * 100;
            switch (comparison) {
                case LIKENESS_AT_LEAST: return percentage >= minLikenessPercentage;
                case LIKENESS_LESS_THAN: return percentage < minLikenessPercentage;
                case HUE_LIKENESS_AT_LEAST: return percentage >= minLikenessPercentage;
                case HUE_LIKENESS_LESS_THAN: return percentage < minLikenessPercentage;
                case SATURATION_LIKENESS_AT_LEAST: return percentage >= minLikenessPercentage;
                case SATURATION_LIKENESS_LESS_THAN: return percentage < minLikenessPercentage;
                case BRIGHTNESS_LIKENESS_AT_LEAST: return percentage >= minLikenessPercentage;
                case BRIGHTNESS_LIKENESS_LESS_THAN: return percentage < minLikenessPercentage;
            }
        }
        return true;
    }

    public ColorCondition(List<RelativeCoordinate> directions, PixelColor desiredColor, int minLikenessPercentage, int comparison) {
        super(directions);
        this.desiredColor = desiredColor;
        this.minLikenessPercentage = minLikenessPercentage;
        this.comparison = comparison;
    }
}
