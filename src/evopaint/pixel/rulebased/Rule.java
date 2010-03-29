/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.Iterator;
import java.util.List;
import javax.swing.RowFilter.ComparisonType;

/**
 *
 * @author tam
 */
public class Rule implements IRule {
    private List<ICondition> conditions;
    private IAction action;

    public List<ICondition> getConditions() {
        return conditions;
    }

    public IAction getAction() {
        return action;
    }

    @Override
    public String toString() {
        String ret = "IF ";
        for (Iterator<ICondition> ii = conditions.iterator(); ii.hasNext();) {
            ICondition condition = ii.next();
            ret += condition.toString();
            if (ii.hasNext()) {
                ret += " AND ";
            }
        }
        ret += " THEN ";
        ret += action.toString();
        return ret;
    }

    public boolean apply(Pixel actor, World world) {
        for (ICondition condition : conditions) {
            if (condition.isMet(actor, world) == false) {
                return false;
            }
        }

        actor.reward((-1) * action.execute(actor, world)); // *(-1) because we "reward" costs
        return true;
    }

    public String validate(Pixel pixel) {
        String ret = null;
        ret = validateEnergyConsumption(pixel);
        if (ret != null) {
            return ret;
        }
        // insert validation2 here
        return "OK";
    }

    private String validateEnergyConsumption(Pixel pixel) {
        if (action.getCost() < 0) {
            return null;
        }
        for (ICondition condition : conditions) {
            if (condition.getClass() == EnergyCondition.class) {
                if (((EnergyCondition)condition).getDirections().contains(RelativeCoordinate.SELF)) {
                    NumberComparisonOperator comparisonOperator = ((EnergyCondition)condition).getComparisonOperator();
                    if (comparisonOperator == NumberComparisonOperator.EQUAL ||
                            comparisonOperator == NumberComparisonOperator.GREATER_THAN ||
                            comparisonOperator == NumberComparisonOperator.GREATER_OR_EQUAL) {
                        if (((EnergyCondition)condition).getEnergyValue() > action.getCost() * action.getDirections().size()) {
                            return null;
                        }
                    }
                }
            }
        }
        return "Warning: You might want to add an energy condition matching on greater than the cost of your desired action or you will kill your pixels";
    }

    public Rule(List<ICondition> conditions, IAction action) {
        this.conditions = conditions;
        this.action = action;
    }
}
