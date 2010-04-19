/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.pixel;

import evopaint.pixel.rulebased.interfaces.IHTML;
import java.awt.Color;
import java.io.Serializable;

// hsb color space is a cylinder with
// radius = saturation, angle = hue and height = brightness

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class PixelColor implements IHTML, Serializable {

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

    public void setInteger(int integer) {
        float [] hsb = null;
        hsb = Color.RGBtoHSB((integer >> 16) & 0xFF, (integer >> 8) & 0xFF, integer & 0xFF, hsb);
        this.hue = hsb[0];
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

    public void setColor(PixelColor theirColor) {
        hue = theirColor.hue;
        saturation = theirColor.saturation;
        brightness = theirColor.brightness;
    }

    @Override
    public String toString() {
        return "#" + Integer.toHexString(Color.HSBtoRGB(hue, saturation, brightness)).substring(2).toUpperCase();
    }

    public String toHTML() {
        String s = toString();
        return "<span style='background: " + s + ";" + (brightness < 0.5 ? "color: #FFFFFF;" : "") + "'>" + s + "</span>";
    }

    public double distanceTo(PixelColor theirColor, ColorDimensions dimensions) {
        double distance = 1;

        if (dimensions.hue && dimensions.saturation && dimensions.brightness) {
                // this is difficult. we need to come up with a distance that
                // is at least somewhat comparable
                // to what we percieve as a "distance" between colors...

                // rule out black and white for hue comparison for the default
                // hue is red which will lead to a wrong distance (in most cases)
                if (saturation == 0f || brightness == 0f ||
                        theirColor.saturation == 0f || theirColor.brightness == 0f) {
                    return (distanceLinear(saturation, theirColor.saturation) +
                        distanceLinear(brightness, theirColor.brightness)
                        ) / 2;
                }

                // hue is everyting. except for when we have very bright
                // or very dark colors
                // the 1:4 division between saturation and brightness is because
                // if you look at the SB colorspace of a given H, you will
                // find that roughly 1/2 of it is "darker", while the other
                // half is a gradient from the color to white giving each
                // "colorful" and "colorless" roughly 1/4 of the total
                // color space
                double maxS = Math.max(saturation, theirColor.saturation);
                double maxB = Math.max(brightness, theirColor.brightness);
                double hueWeight = 1d - ((1d - maxS) * 0.25d + (1d - maxB) * 0.75d) / 2d;
                hueWeight = hueWeight >= 0.15d ? hueWeight : 0.15d; // hue always counts a little
                hueWeight = hueWeight <= 0.85d ? hueWeight : 0.85d; // but never too much
                //System.out.println("hue weight is: " + hueWeight);

                distance = distanceCyclic(hue, theirColor.hue)
                            * hueWeight;

                //System.out.println("hue distance: " + distanceCyclic(hue, theirColor.hue));

                distance = distance
                            + distanceLinear(saturation, theirColor.saturation)
                            * (1d - hueWeight) / 2d;

                distance = distance
                            + distanceLinear(brightness, theirColor.brightness)
                            * (1d - hueWeight) / 2d;

                //System.out.println("distance between " + Integer.toHexString(getInteger()).substring(2).toUpperCase() +
                //        " and " + Integer.toHexString(theirColor.getInteger()).substring(2).toUpperCase() +
                //        " is " + distance);

        } else if (dimensions.hue && dimensions.saturation) {

                // rule out black and white for hue comparison for the default
                // hue is red which will lead to a wrong distance (in most cases)
                if (saturation == 0f || brightness == 0f ||
                        theirColor.saturation == 0f || theirColor.brightness == 0f) {
                    return distanceLinear(saturation, theirColor.saturation);
                }

                // see HSB
                double maxS = Math.max(saturation, theirColor.saturation);
                double maxB = Math.max(brightness, theirColor.brightness);
                double hueWeight = 1d - ((1d - maxS) * 0.25d + (1d - maxB) * 0.75d) / 2d;
                hueWeight = hueWeight >= 0.15d ? hueWeight : 0.15d; // hue always counts a little
                hueWeight = hueWeight <= 0.85d ? hueWeight : 0.85d; // but never too much
                distance = distanceCyclic(hue, theirColor.hue)
                            * hueWeight;
                distance = distance
                            + distanceLinear(saturation, theirColor.saturation)
                            * (1d - hueWeight);

        } else if (dimensions.hue && dimensions.brightness) {
            
                // rule out black and white for hue comparison for the default
                // hue is red which will lead to a wrong distance (in most cases)
                if (saturation == 0f || brightness == 0f ||
                        theirColor.saturation == 0f || theirColor.brightness == 0f) {
                    return distanceLinear(brightness, theirColor.brightness);
                }

                // see HSB
                double maxS = Math.max(saturation, theirColor.saturation);
                double maxB = Math.max(brightness, theirColor.brightness);
                double hueWeight = 1d - ((1d - maxS) * 0.25d + (1d - maxB) * 0.75d) / 2d;
                hueWeight = hueWeight >= 0.15d ? hueWeight : 0.15d; // hue always counts a little
                hueWeight = hueWeight <= 0.85d ? hueWeight : 0.85d; // but never too much
                distance = distanceCyclic(hue, theirColor.hue)
                            * hueWeight;
                distance = distance
                            + distanceLinear(brightness, theirColor.brightness)
                            * (1d - hueWeight);

        } else if (dimensions.saturation && dimensions.brightness) {
                // finally, something easy (I hope)
                distance = (distanceLinear(saturation, theirColor.saturation) +
                        distanceLinear(brightness, theirColor.brightness)
                        ) / 2;

        } else if (dimensions.hue) {
            distance = distanceCyclic(hue, theirColor.hue);

        } else if (dimensions.saturation) {
            distance = distanceLinear(saturation, theirColor.saturation);

        } else if (dimensions.brightness) {
            distance = distanceLinear(brightness, theirColor.brightness);

        } else {
            assert(false);
        }
        
        return distance > 1d ? 1d : distance; // we have some rounding problems
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

    public void mixWith(PixelColor theirColor, float theirShare, ColorDimensions dimensions) {
        //System.out.print(toString() + " + " + theirColor.toString() + " = ");
        
        if (dimensions.hue && dimensions.saturation && dimensions.brightness) {
            // if we are black, white or grey, just take their hue
            if (saturation == 0f || brightness == 0f) {
                hue = theirColor.hue;
            }
            // else if they also have hue, then we mix
            else if (theirColor.saturation != 0f && theirColor.brightness != 0f) {
                hue = (float)mixCyclic(hue, theirColor.hue, theirShare);
            }
            // else they are color less and we keep your hue
            
            saturation = (float)mixLinear(saturation, theirColor.saturation, theirShare);
            brightness = (float)mixLinear(brightness, theirColor.brightness, theirShare);

        } else if (dimensions.hue && dimensions.saturation) {
            // if we are black, white or grey, just take their hue
            if (saturation == 0f || brightness == 0f) {
                hue = theirColor.hue;
            }
            // else if they also have hue, then we mix
            else if (theirColor.saturation != 0f && theirColor.brightness != 0f) {
                hue = (float)mixCyclic(hue, theirColor.hue, theirShare);
            }
            // else they are color less and we keep your hue

            saturation = (float)mixLinear(saturation, theirColor.saturation, theirShare);

        } else if (dimensions.hue && dimensions.brightness) {
            // if we are black, white or grey, just take their hue
            if (saturation == 0f || brightness == 0f) {
                hue = theirColor.hue;
            }
            // else if they also have hue, then we mix
            else if (theirColor.saturation != 0f && theirColor.brightness != 0f) {
                hue = (float)mixCyclic(hue, theirColor.hue, theirShare);
            }
            // else they are color less and we keep your hue

            brightness = (float)mixLinear(brightness, theirColor.brightness, theirShare);

        } else if (dimensions.saturation && dimensions.brightness) {
            saturation = (float)mixLinear(saturation, theirColor.saturation, theirShare);
            brightness = (float)mixLinear(brightness, theirColor.brightness, theirShare);

        } else if (dimensions.hue) {
            hue = (float)mixCyclic(hue, theirColor.hue, theirShare);

        } else if (dimensions.saturation) {
            saturation = (float)mixLinear(saturation, theirColor.saturation, theirShare);

        } else if (dimensions.brightness) {
            brightness = (float)mixLinear(brightness, theirColor.brightness, theirShare);

        } else {
            assert(false);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PixelColor other = (PixelColor) obj;
        if (this.hue != other.hue) {
            return false;
        }
        if (this.saturation != other.saturation) {
            return false;
        }
        if (this.brightness != other.brightness) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Float.floatToIntBits(this.hue);
        hash = 53 * hash + Float.floatToIntBits(this.saturation);
        hash = 53 * hash + Float.floatToIntBits(this.brightness);
        return hash;
    }

    public PixelColor(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public PixelColor(int integer) {
        setInteger(integer);
    }

    public PixelColor(PixelColor pixelColor) {
        this.hue = pixelColor.hue;
        this.saturation = pixelColor.saturation;
        this.brightness = pixelColor.brightness;
    }
}
