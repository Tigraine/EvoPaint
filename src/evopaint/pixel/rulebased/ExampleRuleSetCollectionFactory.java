/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.pixel.ColorDimensions;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tam
 */
public class ExampleRuleSetCollectionFactory {

    public static RuleSetCollection createCollectionSimple() {
        String name = "Simple";
        String description = "A collection of simple examples. If you have started EvoPaint " +
                "and are a bit clueless about what it can do, check out these rule sets " +
                "to get an idea about how they perform. Try modifying and playing with them, " +
                "it's fun!";
        return new RuleSetCollection(name, description);
    }

    public static List<RuleSet> createRuleSetsSimple() {
        String name = null;
        String description = null;
        List<IRule> rules = null;
        List<ICondition> conditions = null;
        IAction action = null;
        List<RuleSet> ruleSets = new ArrayList();

        name = "Simple Color Assimilation";
        description = "This one-line rule set will initiate a nice " +
                "to watch and very colorful spectacle if you apply it to randomly colored " +
                "pixels. For the full effect, apply this rule set to a large sized " +
                "fairy dust brush. Since the action requires no cost per default " +
                " you can sit back, relax and enjoy the show until you get bored." +
                "<br><br>Don't miss to use a medium density " +
                "brush or fill action once the development nears an end for some extra fun!";
        rules = new ArrayList<IRule>();
        conditions = new ArrayList<ICondition>();
        List<RelativeCoordinate> directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new TrueCondition());
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.WEST);
        directions.add(RelativeCoordinate.NORTH);
        directions.add(RelativeCoordinate.SOUTH);
        directions.add(RelativeCoordinate.EAST);
        action = new AssimilationAction(0, directions, new ColorDimensions(true, true, true), (byte)50);
        rules.add(new Rule(conditions, action));
        ruleSets.add(new RuleSet(name, description , rules));

        return ruleSets;
    }
}

