/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package evopaint.util;

import evopaint.util.logging.Logger;


/**
 *
 * @author tam
 */
public class Color {
    public static final int COMPARE_BY_INTEGER = 0;
    public static final int COMPARE_BY_RGB = 1;
    public static final int COMPARE_BY_HSB = 2;
    public static final int COMPARE_BY_RED = 3;
    public static final int COMPARE_BY_BLUE = 4;
    public static final int COMPARE_BY_GREEN = 5;
    public static final int COMPARE_BY_HUE = 6;
    public static final int COMPARE_BY_SATURATION = 7;
    public static final int COMPARE_BY_BRIGHTNESS = 8;

    public static final int MIX_RGB = 0;
    public static final int MIX_HSB = 1;

    private int integer;

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = 0xFF000000 |  integer;
    }

    public short [] getRGB() {
        short [] ret = new short[3];
        ret[0] = (short) ((this.integer >> 16) & 0xFF);
        ret[1] = (short) ((this.integer >> 8) & 0xFF);
        ret[2] = (short) (this.integer & 0xFF);
        return ret;
    }

    public void setRGB(short [] rgb) {
        this.integer = 0 | rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }

    public float [] getHSB() {
        return java.awt.Color.RGBtoHSB((this.integer >> 16) & 0xFF,
                (this.integer >> 8) & 0xFF,
                this.integer & 0xFF,
                null);
    }

    // NOTE: javas Color.HSBtoRGB results in 0xFF alpha value
    //      so any alpha channel will be lost after calling this method
    public void setHSB(float [] hsb) {
        assert(hsb.length == 3);
        this.integer = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    public boolean isLike(Color color, double minLikeliness, int mode) {
        switch(mode) {
            case Color.COMPARE_BY_INTEGER: return isLikeInteger(color, minLikeliness);
            case Color.COMPARE_BY_RGB: return isLikeRGB(color, minLikeliness);
            case Color.COMPARE_BY_HSB: return isLikeHSB(color, minLikeliness);
            case Color.COMPARE_BY_RED: return isLikeRed(color, minLikeliness);
            case Color.COMPARE_BY_GREEN: return isLikeGreen(color, minLikeliness);
            case Color.COMPARE_BY_BLUE: return isLikeBlue(color, minLikeliness);
            case Color.COMPARE_BY_HUE: return isLikeHue(color, minLikeliness);
            case Color.COMPARE_BY_SATURATION: return isLikeSaturation(color, minLikeliness);
            case Color.COMPARE_BY_BRIGHTNESS: return isLikeBrightness(color, minLikeliness);
            default: Logger.log.error("Invalid mode: %s", Integer.valueOf(mode));
                    System.exit(1);
        }
        return false;
    }

    private boolean isLikeInteger(Color color, double minLikeliness) {
        int diff = Math.abs(this.integer & 0x00FFFFFF - color.getInteger() & 0x00FFFFFF);
        if (0x00FFFFFF - diff >= minLikeliness * 0x00FFFFFF) {
            return true;
        }
        return false;
    }

    private boolean isLikeRGB(Color color, double minLikeliness) {
        return  isLikeRed(color, minLikeliness) &&
                isLikeGreen(color, minLikeliness) &&
                isLikeBlue(color, minLikeliness);
    }

    private boolean isLikeRed(Color color, double minLikeliness) {
        return isLikeRGBComponent(color, minLikeliness, 16);
    }

    private boolean isLikeGreen(Color color, double minLikeliness) {
        return isLikeRGBComponent(color, minLikeliness, 8);
    }

    private boolean isLikeBlue(Color color, double minLikeliness) {
        return isLikeRGBComponent(color, minLikeliness, 0);
    }

    private boolean isLikeRGBComponent(Color color, double minLikeliness, int shiftAmount) {
        int diff = ((integer >> shiftAmount) & 0xFF) - ((color.getInteger() >> shiftAmount) & 0xFF);
        return 0xFF - diff >= minLikeliness * 0xFF;
    }

    private boolean isLikeHSB(Color color, double minLikeliness) {
        /*
         * this would be nice, but slow due to the multiple calculations of HSB
         *
         * return  isLikeHue(color, minLikeliness) &&
         *       isLikeSaturation(color, minLikeliness) &&
         *       isLikeBrightness(color, minLikeliness);
         */
        float [] ourHSB = this.getHSB();
        float [] theirHSB = color.getHSB();
        float dh = Math.abs(ourHSB[0] - theirHSB[0]);
        float ds = Math.abs(ourHSB[1] - theirHSB[1]);
        float db = Math.abs(ourHSB[2] - theirHSB[2]);
        return (dh > 1 - dh) ? (dh >= minLikeliness) : (1 - dh >= minLikeliness) &&
                1 - ds >= minLikeliness &&
                1 - db >= minLikeliness;
    }

    private boolean isLikeHue(Color color, double minLikeliness) {
        float delta = Math.abs(this.getHSB()[0] - color.getHSB()[0]);
        return (delta > 1 - delta) ? (delta >= minLikeliness) : (1 - delta >= minLikeliness);
    }

    private boolean isLikeSaturation(Color color, double minLikeliness) {
        return 1 - Math.abs(this.getHSB()[1] - color.getHSB()[1]) >= minLikeliness;
    }

    private boolean isLikeBrightness(Color color, double minLikeliness) {
        return 1 - Math.abs(this.getHSB()[2] - color.getHSB()[2]) >= minLikeliness;
    }

    public void mixIn(Color them, float theirShare, int mode) {
        switch(mode) {
            case MIX_RGB: mixInRGB(them, theirShare);
            break;
            case MIX_HSB: mixInHSB(them, theirShare);
            break;
        }
    }
    /* XXX this is currently just 50/50 mixing, but I have a feeling that if
     * we included an evolution finding the optimal mixing percentage, it would
     * turn out to be 50/50 anyways...
     */
    private void mixInRGB(Color them, float theirShare) {
        // TODO theirShare unused!
        short [] c1rgb = them.getRGB();
        short [] c2rgb = this.getRGB();
        short [] mixrgb = new short[3];

        for (int i = 0; i < 3; i++)
            mixrgb[i] = (short)(Math.min(c1rgb[i], c2rgb[i]) + Math.abs(c1rgb[i] - c2rgb[i]) / 2);

        this.setRGB(mixrgb);
    }

    private void mixInHSB(Color them, float theirShare) {
        float[] theirHSB = them.getHSB();
        float[] ourHSB = this.getHSB();
        float[] mixhsb = new float[3];

        mixhsb[0] = mixCyclic(theirHSB[0], ourHSB[0], theirShare);
        mixhsb[1] = mixLinear(theirHSB[1], ourHSB[1], theirShare);
        mixhsb[2] = mixLinear(theirHSB[2], ourHSB[2], theirShare);

        this.setHSB(mixhsb);
    }

    private float mixCyclic(float a, float b, float shareOfA) {
        float ret = 0.0f;
        float min = Math.min(a, b);
        float delta = Math.abs(a - b);
        boolean isWrapped = false;
        if (delta > 1 - delta) {
            isWrapped = true;
            delta = 1 - delta;
        }
        if (min == a) {
            ret = isWrapped ? min - delta * (1 - shareOfA) : min + delta * (1 - shareOfA);
        } else {
            ret = isWrapped ? min - delta * shareOfA : min + delta * shareOfA;
        }
        if (ret < 0) {
            ret = ret + 1;
        }
        return ret;
    }

    private float mixLinear(float a, float b, float shareOfA) {
        float min = Math.min(a, b);
        float delta = Math.abs(a - b);

        if (min == a) {
            return min + delta * (1 - shareOfA);
        } else {
            return min + delta * shareOfA;
        }
    }


    public Color(int color) {
        this.integer = 0xFF000000 | color;
    }
}
