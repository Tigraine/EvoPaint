/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package evopaint.pixel;

import java.awt.Color;


/**
 *
 * @author tam
 */
public class PixelColor {
    public static final int HSB = 0;
    public static final int H = 1;
    public static final int S = 2;
    public static final int B = 3;
    public static final int HS = 4;
    public static final int SB = 5;
    public static final int BH = 6;
    
    private float [] hsb;

    public float[] getHSB() {
        return hsb;
    }

    public float getH() {
        return hsb[0];
    }
    
    public float getS() {
        return hsb[1];
    }
        
    public float getB() {
        return hsb[2];
    }

    public int getInteger() {
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    public void setHSB(float[] hsb) {
        this.hsb = hsb;
    }

    @Override
    public String toString() {
        return "#" + Integer.toHexString(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])).substring(2).toUpperCase();
    }

    public static double likeness(PixelColor ourColor, PixelColor theirColor, int mode) {
        float[] ourHSB = ourColor.getHSB();
        float[] theirHSB = theirColor.getHSB();

        switch (mode) {
            case HSB:
                return (compareCyclic(ourHSB[0], theirHSB[0]) +
                        compareLinear(ourHSB[1], theirHSB[1]) +
                        compareLinear(ourHSB[2], theirHSB[2])
                        ) / 3;
            case H:
                return compareCyclic(ourHSB[0], theirHSB[0]);
            case S:
                return compareLinear(ourHSB[1], theirHSB[1]);
            case B:
                return compareLinear(ourHSB[2], theirHSB[2]);
            case HS:
                return (compareCyclic(ourHSB[0], theirHSB[0]) +
                        compareLinear(ourHSB[1], theirHSB[1])
                        ) / 2;
            case SB:
                return (compareLinear(ourHSB[1], theirHSB[1]) +
                        compareLinear(ourHSB[2], theirHSB[2])
                        ) / 2;
            case BH:
                return (compareLinear(ourHSB[2], theirHSB[2]) +
                        compareCyclic(ourHSB[0], theirHSB[0])
                        ) / 2;
        }
        assert(false);
        return 0;
    }

    private static double compareCyclic(double a, double b) {
        double delta = Math.abs(a - b);
        return Math.min(delta, 1 - delta);
    }

    private static double compareLinear(double a, double b) {
        return Math.abs(a - b);
    }

    public static float [] mix(PixelColor us, PixelColor them, float theirShare, int mode) {
        //System.out.print(us.toString() + " + " + them.toString() + " = ");
        float [] ret = new float[3];
        float[] ourHSB = us.getHSB();
        float[] theirHSB = them.getHSB();
        
        // hsb color space is a cylinder with
        // radius = saturation, angle = hue and height = brightness

        // use atan2 to calculate the angle (hue)
        //double x1 = Math.cos(ourHSB[0] * 360);
        //double y1 = Math.sin(ourHSB[0] * 360);
        //double x2 = Math.cos(theirHSB[0] * 360);
        //double y2 = Math.sin(theirHSB[0] * 360);
        //double x = Math.min(x1, x2) + Math.abs(x1 - x2) / 2;
        //double y = Math.min(y1, y2) + Math.abs(y1 - y2) / 2;
        //ret[0] = (float)(Math.atan2(y, x) / 360);
        ret[0] = mixCyclic(ourHSB[0], theirHSB[0], theirShare);

        // saturation and brightness are plain cartesian coordinates
        ret[1] = (float)mixLinear(ourHSB[1], theirHSB[1], theirShare);
        ret[2] = (float)mixLinear(ourHSB[2], theirHSB[2], theirShare);
        //System.out.println(Integer.toHexString(Color.HSBtoRGB(ret[0], ret[1], ret[2])));
        return ret;
    }

    private static float mixCyclic(float a, float b, float shareOfB) {
        float ret = 0.0f;
        float min = Math.min(a, b);
        float delta = Math.abs(a - b);
        boolean isWrapped = false;
        if (delta > 1 - delta) {
            isWrapped = true;
            delta = 1 - delta;
        }
        if (min == b) {
            ret = isWrapped ? min - delta * (1 - shareOfB) : min + delta * (1 - shareOfB);
        } else {
            ret = isWrapped ? min - delta * shareOfB : min + delta * shareOfB;
        }
        if (ret < 0) {
            ret = ret + 1;
        }
        return ret;
    }

    private static double mixLinear(double us, double them, double theirShare) {
        double min = Math.min(us, them);
        double delta = Math.abs(us - them);

        if (min == them) {
            return min + delta * (1 - theirShare);
        } else {
            return min + delta * theirShare;
        }
    }

    public PixelColor(float[] hsb) {
        this.hsb = hsb;
    }

    public PixelColor(int integer) {
        this.hsb = Color.RGBtoHSB(integer >> 16 & 0xFF, integer >> 8 & 0xFF, integer & 0xFF, this.hsb);
    }

    public PixelColor(PixelColor pixelColor) {
        this.hsb = pixelColor.hsb.clone();
    }
}
