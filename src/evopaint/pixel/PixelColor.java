/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package evopaint.pixel;

import evopaint.interfaces.IRandomNumberGenerator;
import java.awt.Color;

// hsb color space is a cylinder with
// radius = saturation, angle = hue and height = brightness

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
    public static final int HB = 5;
    public static final int SB = 6;
    
    private float hue;
    private float saturation;
    private float brightness;

    public float[] getHSB() {
        return new float [] {hue, saturation, brightness};
    }

    public float getBrightness() {
        return brightness;
    }

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setHSB(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public void setInteger(int integer, IRandomNumberGenerator rng) {
        float [] hsb = null;
        hsb = Color.RGBtoHSB(integer >> 16 & 0xFF, integer >> 8 & 0xFF, integer & 0xFF, hsb);
        this.hue = (integer & 0xFFFFFF) == 0 || // because red is default and you wonder why your picture becomes all red..
                (integer & 0xFFFFFF) == 0xFFFFFF ? rng.nextFloat() : hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    public int getInteger() {
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public void setHSB(float[] hsb) {
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    public String toString() {
        return "#" + Integer.toHexString(Color.HSBtoRGB(hue, saturation, brightness)).substring(2).toUpperCase();
    }

    public double distanceTo(PixelColor theirColor, int dimensions) {
        double distance = 1;
        
        switch (dimensions) {
            case HSB:
                // this is difficult. we need to come up with a distance that
                // is at least somewhat comparable
                // to what we percieve as a "distance" between colors...
                
                // let us define black as black
                
                
                // let's start with hue, for that is what makes color colorful
                distance = distanceCyclic(hue, theirColor.getHue());

                // if one of the colors is very colorful, it does not matter
                // whether the other is almost white or not, so we take the biggest one
                // the same holds true for very dark colors
                // now we need to find some means to weight the color distance
                // with these. let us try multiplying them for now
                distance = distance * Math.max(saturation, theirColor.getSaturation())
                            * Math.max(brightness, theirColor.getBrightness());
                break;
            case H:
                distance = distanceCyclic(hue, theirColor.getHue());
                break;
            case S:
                distance = distanceLinear(saturation, theirColor.getSaturation());
                break;
            case B:
                distance = distanceLinear(brightness, theirColor.getBrightness());
                break;
            case HS:
                // see HSB
                distance = distanceCyclic(hue, theirColor.getHue());
                distance *= Math.max(saturation, theirColor.getSaturation());
                break;
            case HB:
                // see HSB
                distance = distanceCyclic(hue, theirColor.getHue());
                distance *= Math.max(brightness, theirColor.getBrightness());
                break;
            case SB:
                // finally, something easy (I hope)
                distance = (distanceLinear(saturation, theirColor.getSaturation()) +
                        distanceLinear(brightness, theirColor.getBrightness())
                        ) / 2;
                break;
            default: assert(false);
        }
        
        return distance;
    }

    private static double distanceCyclic(double a, double b) {
        //System.out.print("distance between " + a + " and " + b + " is ");
        double delta = Math.abs(a - b);
        //System.out.println(delta);
        return Math.min(delta, 1 - delta) * 2; // * 2 to norm to max distance 1
    }

    private static double distanceLinear(double a, double b) {
        return Math.abs(a - b);
    }

    public void mixWith(PixelColor theirColor, float theirShare, int dimensions) {
        //System.out.print(toString() + " + " + theirColor.toString() + " = ");
        switch (dimensions) {
            case HSB:
                hue = (float)mixCyclic(hue, theirColor.getHue(), theirShare);
                saturation = (float)mixLinear(saturation, theirColor.getSaturation(), theirShare);
                brightness = (float)mixLinear(brightness, theirColor.getBrightness(), theirShare);
                break;
            case H:
                hue = (float)mixCyclic(hue, theirColor.getHue(), theirShare);
                break;
            case S:
                saturation = (float)mixLinear(saturation, theirColor.getSaturation(), theirShare);
                break;
            case B:
                brightness = (float)mixLinear(brightness, theirColor.getBrightness(), theirShare);
                break;
            case HS:
                hue = (float)mixCyclic(hue, theirColor.getHue(), theirShare);
                saturation = (float)mixLinear(saturation, theirColor.getSaturation(), theirShare);
                break;
            case HB:
                hue = (float)mixCyclic(hue, theirColor.getHue(), theirShare);
                brightness = (float)mixLinear(brightness, theirColor.getBrightness(), theirShare);
                break;
            case SB:
                saturation = (float)mixLinear(saturation, theirColor.getSaturation(), theirShare);
                brightness = (float)mixLinear(brightness, theirColor.getBrightness(), theirShare);
                break;
            default: assert(false);
        }
        //System.out.println(toString());
    }

    private static double mixCyclic(double a, double b, double shareOfB) {
        double ret = 0.0f;
        double min = Math.min(a, b);
        double delta = Math.abs(a - b);
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

    public PixelColor(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public PixelColor(int integer, IRandomNumberGenerator rng) {
        setInteger(integer, rng);
    }

    public PixelColor(PixelColor pixelColor) {
        this.hue = pixelColor.hue;
        this.saturation = pixelColor.saturation;
        this.brightness = pixelColor.brightness;
    }
}
