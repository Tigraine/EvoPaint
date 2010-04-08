package evopaint;

import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.interfaces.IHTML;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Paint implements IHTML {
    public static final int COLOR = 0;
    public static final int FAIRY_DUST = 1;
    public static final int NO_COLOR = 2;

    private int colorMode;
    private PixelColor color;
    private RuleSet ruleSet;

    public String toHTML() {
        String ret = new String();
        switch (colorMode) {
            case COLOR: ret += color.toHTML();
            break;
            case FAIRY_DUST: ret += "Fairy Dust";
            break;
            case NO_COLOR: ret += "No Color";
            break;
            default: assert(false);
        }
        ret += " - ";
        if (ruleSet != null) {
            ret += "\"" + ruleSet.getName() + "\"";
        } else {
            ret += "No Rule Set";
        }
        return ret;
    }

    public PixelColor getColor() {
        return color;
    }

    public int getColorMode() {
        return colorMode;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public Paint(int colorMode, PixelColor color, RuleSet ruleSet) {
        this.colorMode = colorMode;
        this.color = color;
        this.ruleSet = ruleSet;
    }
}
