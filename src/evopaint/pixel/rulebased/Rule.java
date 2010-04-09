/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.World;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.actions.IdleAction;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tam
 */
public class Rule implements IRule, IHTML, ICopyable {
    private List<ICondition> conditions;
    private IAction action;

    public List<ICondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<ICondition> conditions) {
        this.conditions = conditions;
    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
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

    public String toHTML() {
        String ret = "<span style='color: #0000E6; font-weight: bold;'>IF</span> ";
        for (Iterator<ICondition> ii = conditions.iterator(); ii.hasNext();) {
            ICondition condition = ii.next();
            ret += condition.toHTML();
            if (ii.hasNext()) {
                ret += " <span style='color: #0000E6; font-weight: bold;'>AND</span> ";
            }
        }
        ret += " <span style='color: #0000E6; font-weight: bold;'>THEN</span> ";
        ret += action.toHTML();
        return ret;
    }

    public boolean apply(Pixel actor, Configuration configuration) {
        for (ICondition condition : conditions) {
            if (condition.isMet(actor, configuration.world) == false) {
                return false;
            }
        }

        actor.reward((-1) * action.execute(actor, configuration)); // *(-1) because we "reward" costs
        return true;
    }

    public Rule getCopy() {
        Rule newRule = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newRule = (Rule) in.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return newRule;
    }

    public Rule(List<ICondition> conditions, IAction action) {
        this.conditions = conditions;
        this.action = action;
    }

    public Rule() {
        this.conditions = new ArrayList<ICondition>();
        this.conditions.add(new TrueCondition());
        this.action = new IdleAction();
    }
}
