/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.misc;

/**
 *
 * @author tam
 */
public class ColorMixMode {
    public static final int MODE_RGB = 0;
    public static final int MODE_HSB = 1;

    public static final ColorMixMode RGB = new ColorMixMode(MODE_RGB, "RGB");
    public static final ColorMixMode HSB = new ColorMixMode(MODE_HSB, "HSB");

    private int mode;
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public int getMode() {
        return mode;
    }

    private ColorMixMode(int mode, String name) {
        this.mode = mode;
        this.name = name;
    }
}
