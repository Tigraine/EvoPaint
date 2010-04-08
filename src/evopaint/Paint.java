package evopaint;

import evopaint.gui.util.ColorIcon;
import evopaint.gui.util.FairyDustIcon;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleSet;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Paint {
    public static final int COLOR = 0;
    public static final int FAIRY_DUST = 1;
    public static final int NO_COLOR = 2;

    private Configuration configuration;
    private int colorMode;
    private PixelColor color;
    private RuleSet ruleSet;

    public JMenuItem toMenuItem() {
        Icon icon = null;
        switch (colorMode) {
            case COLOR: icon = new ColorIcon(16, 16, new Color(color.getInteger()));
            break;
            case FAIRY_DUST: icon = new FairyDustIcon(configuration, 16, 16);
            break;
            case NO_COLOR: icon = null;
            break;
            default: assert(false);
        }
        String ruleSetName = new String();
        if (ruleSet != null) {
            ruleSetName = ruleSet.getName();
        }
        JMenuItem ret = new JMenuItem(ruleSetName, icon);
        /*switch (colorMode) {
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
        }*/
        
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

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object object) {
        return hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.colorMode;
        hash = 29 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 29 * hash + (this.ruleSet != null ? this.ruleSet.hashCode() : 0);
        return hash;
    }

    public Paint(Configuration configuration, int colorMode, PixelColor color, RuleSet ruleSet) {
        this.configuration = configuration;
        this.colorMode = colorMode;
        this.color = color;
        this.ruleSet = ruleSet;
    }
}
