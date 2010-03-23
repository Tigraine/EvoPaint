/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.misc;

import evopaint.pixel.PixelColor;
import evopaint.util.logging.Logger;

/**
 *
 * @author tam
 */
public class ColorComparisonOperator {
    private static final int TYPE_EQUAL = 0;
    private static final int TYPE_NOT_EQUAL = 1;

    private static final int TYPE_MINIMUM_RGB_LIKENESS = 2;
    private static final int TYPE_RGB_LIKENESS_LESS_THAN = 3;
    private static final int TYPE_MINIMUM_HSB_LIKENESS = 4;
    private static final int TYPE_HSB_LIKENESS_LESS_THAN = 5;

    private static final int TYPE_MINIMUM_RED_LIKENESS = 6;
    private static final int TYPE_RED_LIKENESS_LESS_THAN = 7;
    private static final int TYPE_MINIMUM_GREEN_LIKENESS = 8;
    private static final int TYPE_GREEN_LIKENESS_LESS_THAN = 9;
    private static final int TYPE_MINIMUM_BLUE_LIKENESS = 10;
    private static final int TYPE_BLUE_LIKENESS_LESS_THAN = 11;

    private static final int TYPE_MINIMUM_HUE_LIKENESS = 12;
    private static final int TYPE_HUE_LIKENESS_LESS_THAN = 13;
    private static final int TYPE_MINIMUM_SATURATION_LIKENESS = 14;
    private static final int TYPE_SATURATION_LIKENESS_LESS_THAN = 15;
    private static final int TYPE_MINIMUM_BRIGHTNESS_LIKENESS = 16;
    private static final int TYPE_BRIGHTNESS_LIKENESS_LESS_THAN = 17;


    public static final ColorComparisonOperator EQUAL = new ColorComparisonOperator(TYPE_EQUAL);
    public static final ColorComparisonOperator NOT_EQUAL = new ColorComparisonOperator(TYPE_NOT_EQUAL);

    public static final ColorComparisonOperator MINIMUM_RGB_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_RGB_LIKENESS);
    public static final ColorComparisonOperator RGB_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_RGB_LIKENESS_LESS_THAN);
    public static final ColorComparisonOperator MINIMUM_HSB_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_HSB_LIKENESS);
    public static final ColorComparisonOperator HSB_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_HSB_LIKENESS_LESS_THAN);

    public static final ColorComparisonOperator MINIMUM_RED_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_RED_LIKENESS);
    public static final ColorComparisonOperator RED_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_RED_LIKENESS_LESS_THAN);
    public static final ColorComparisonOperator MINIMUM_GREEN_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_GREEN_LIKENESS);
    public static final ColorComparisonOperator GREEN_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_GREEN_LIKENESS_LESS_THAN);
    public static final ColorComparisonOperator MINIMUM_BLUE_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_BLUE_LIKENESS);
    public static final ColorComparisonOperator BLUE_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_BLUE_LIKENESS_LESS_THAN);

    public static final ColorComparisonOperator MINIMUM_HUE_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_HUE_LIKENESS);
    public static final ColorComparisonOperator HUE_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_HUE_LIKENESS_LESS_THAN);
    public static final ColorComparisonOperator MINIMUM_SATURATION_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_SATURATION_LIKENESS);
    public static final ColorComparisonOperator SATURATION_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_SATURATION_LIKENESS_LESS_THAN);
    public static final ColorComparisonOperator MINIMUM_BRIGHTNESS_LIKENESS = new ColorComparisonOperator(TYPE_MINIMUM_BRIGHTNESS_LIKENESS);
    public static final ColorComparisonOperator BRIGHTNESS_LIKENESS_LESS_THAN = new ColorComparisonOperator(TYPE_BRIGHTNESS_LIKENESS_LESS_THAN);

    private int type;

    @Override
    public String toString() {
        switch (this.type) {
            case TYPE_EQUAL: return "==";
            case TYPE_NOT_EQUAL: return "!=";
            case TYPE_MINIMUM_RGB_LIKENESS: return "RGB likeness of >=";
            case TYPE_RGB_LIKENESS_LESS_THAN: return "RGB likeness of <=";
            case TYPE_MINIMUM_HSB_LIKENESS: return "HSB likeness of >=";
            case TYPE_HSB_LIKENESS_LESS_THAN: return "HSB likeness of <";

            case TYPE_MINIMUM_RED_LIKENESS: return "red likeness of >=";
            case TYPE_RED_LIKENESS_LESS_THAN: return "red likeness of <";
            case TYPE_MINIMUM_GREEN_LIKENESS: return "green likeness of >=";
            case TYPE_GREEN_LIKENESS_LESS_THAN: return "green likeness of <";
            case TYPE_MINIMUM_BLUE_LIKENESS: return "blue likeness of >=";
            case TYPE_BLUE_LIKENESS_LESS_THAN: return"blue likeness of <";

            case TYPE_MINIMUM_HUE_LIKENESS: return "hue likeness of >=";
            case TYPE_HUE_LIKENESS_LESS_THAN: return "hue likeness of <";
            case TYPE_MINIMUM_SATURATION_LIKENESS: return "saturation likeness of >=";
            case TYPE_SATURATION_LIKENESS_LESS_THAN: return "saturation likeness of <";
            case TYPE_MINIMUM_BRIGHTNESS_LIKENESS: return "brightness likeness of >=";
            case TYPE_BRIGHTNESS_LIKENESS_LESS_THAN: return "brightness likeness of <";
        }
        return null;
    }

    public boolean compare(PixelColor a, PixelColor b, int likenessPercentage) {
        double likeness = ((double)likenessPercentage) / 100;
        switch (this.type) {
            case TYPE_EQUAL: return a.equals(b);
            case TYPE_NOT_EQUAL: return !a.equals(b);
            case TYPE_MINIMUM_RGB_LIKENESS: return a.isLikeRGB(b, likeness);
            case TYPE_RGB_LIKENESS_LESS_THAN: return !a.isLikeRGB(b, likeness);
            case TYPE_MINIMUM_HSB_LIKENESS: return a.isLikeHSB(b, likeness);
            case TYPE_HSB_LIKENESS_LESS_THAN: return !a.isLikeHSB(b, likeness);

            case TYPE_MINIMUM_RED_LIKENESS: return a.isLikeRed(b, likeness);
            case TYPE_RED_LIKENESS_LESS_THAN: return !a.isLikeRed(b, likeness);
            case TYPE_MINIMUM_GREEN_LIKENESS: return a.isLikeGreen(b, likeness);
            case TYPE_GREEN_LIKENESS_LESS_THAN: return !a.isLikeGreen(b, likeness);
            case TYPE_MINIMUM_BLUE_LIKENESS: return a.isLikeBlue(b, likeness);
            case TYPE_BLUE_LIKENESS_LESS_THAN: return !a.isLikeBlue(b, likeness);

            case TYPE_MINIMUM_HUE_LIKENESS: return a.isLikeHue(b, likeness);
            case TYPE_HUE_LIKENESS_LESS_THAN: return !a.isLikeHue(b, likeness);
            case TYPE_MINIMUM_SATURATION_LIKENESS: return a.isLikeSaturation(b, likeness);
            case TYPE_SATURATION_LIKENESS_LESS_THAN: return !a.isLikeSaturation(b, likeness);
            case TYPE_MINIMUM_BRIGHTNESS_LIKENESS: return a.isLikeSaturation(b, likeness);
            case TYPE_BRIGHTNESS_LIKENESS_LESS_THAN: return !a.isLikeSaturation(b, likeness);
        }
        Logger.log.error("tried to compare with unknown type", new Object());
        return false;
    }

    private ColorComparisonOperator(int type) {
        this.type = type;
    }
}
