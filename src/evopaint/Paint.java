package evopaint;

import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.actions.IdleAction;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Paint implements IHTML {
    public static final int COLOR = 0;
    public static final int FAIRY_DUST = 1;
    public static final int USE_EXISTING = 2;

    private int mode;
    private PixelColor color;
    private RuleSet ruleSet;

    public String toHTML() {
        String ret = new String();
        switch (mode) {
            case COLOR: ret += color.toHTML();
            break;
            case FAIRY_DUST: ret += "Fairy Dust";
            break;
            case USE_EXISTING: ret += "Use Existing";
            break;
            default: assert(false);
        }
        return ret + " - \"" + ruleSet.getName() + "\"";
    }

    public PixelColor getColor() {
        return color;
    }

    public int getMode() {
        return mode;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public static RuleSet noRuleSet() {
        List<IRule> rules = new ArrayList<IRule>();

        List<ICondition> conditions = new ArrayList<ICondition>();
        List<RelativeCoordinate> directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new TrueCondition());
        IAction action = new IdleAction();
        rules.add(new Rule(conditions, action));

        return new RuleSet("No RuleSet", "You should not be able to read this, well okay by digging the source you should. In that case: Welcome to the EvoPaint source code, enjoy your stay and copy all you like!", rules);
    }

    public Paint(int mode, PixelColor color, RuleSet ruleSet) {
        this.mode = mode;
        this.color = color;
        this.ruleSet = ruleSet;
    }
}
