/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.pixel.ColorDimensions;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.NoCondition;
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
public class ExampleRuleSetFactory {
    public static RuleSet createSimpleColorAssimilation() {
        List<IRule> rules = new ArrayList<IRule>();
        List<ICondition> conditions = new ArrayList<ICondition>();
        List<RelativeCoordinate> directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new NoCondition());
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.WEST);
        directions.add(RelativeCoordinate.NORTH);
        directions.add(RelativeCoordinate.SOUTH);
        directions.add(RelativeCoordinate.EAST);
        IAction action = new AssimilationAction(0, directions, new ColorDimensions(true, true, true));
        rules.add(new Rule(conditions, action));

        String name = "Simple Color Assimilation";
        String description = "This one-line rule set will initiate a nice " +
                "to watch colorful spectacle if you apply it to randomly colored" +
                "pixels. For the full effect, apply this rule set to a large sized" +
                "fairy dust brush. Since there are no cost involved, you can enjoy" +
                "the development to no end.<br><br>Don't miss to use a medium density" +
                "brush or fill action once the development nears an end for extra fun!";

        return new RuleSet(name, description , rules);
    }
}
