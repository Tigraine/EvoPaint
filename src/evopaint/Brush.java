/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.RuleSet;

/**
 *
 * @author tam
 */
public class Brush {
    public static final int COLORMODE_COLOR = 0;
    public static final int COLORMODE_FAIRY_DUST = 1;
    public static final int COLORMODE_USE_EXISTING = 2;

    private int colorInteger;
    private int brushSize;
    private RuleSet ruleSet;

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public int getColorInteger() {
        return colorInteger;
    }

    public void setColorInteger(int colorInteger) {
        this.colorInteger = colorInteger;
    }

    public Brush() {
    }
}
