/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.actions.IdleAction;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.targeting.MultiTarget;
import evopaint.pixel.rulebased.targeting.SingleTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedConditionTarget;
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
    private List<Condition> conditions;
    private Action action;

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        String ret = "IF ";
        for (Iterator<Condition> ii = conditions.iterator(); ii.hasNext();) {
            Condition condition = ii.next();
            if (false == condition instanceof TrueCondition) {
                ret += condition.getTarget().toString();
                ret += " ";
            }
            ret += condition.toString();
            if (ii.hasNext()) {
                ret += " AND ";
            }
        }
        ret += " THEN ";
        ret += action.toString();
        if (false == action instanceof IdleAction) {
            ret += " ";
            ret += action.getTarget().toString();
        }
        return ret;
    }

    public String toHTML() {
        String ret = "<span style='color: #0000E6; font-weight: bold;'>IF</span> ";
        for (Iterator<Condition> ii = conditions.iterator(); ii.hasNext();) {
            Condition condition = ii.next();
            if (false == condition instanceof TrueCondition) {
                ret += condition.getTarget().toHTML();
                ret += " ";
            }
            ret += condition.toHTML();
            if (ii.hasNext()) {
                ret += " <span style='color: #0000E6; font-weight: bold;'>AND</span> ";
            }
        }
        ret += " <span style='color: #0000E6; font-weight: bold;'>THEN</span> ";
        
        ret += action.toHTML();
        if (false == action instanceof IdleAction) {
            ret += " ";
            ret += action.getTarget().toHTML();
        }
        return ret;
    }

    public boolean apply(Pixel actor, Configuration configuration) {
        for (Condition condition : conditions) {
            if (condition.isMet(actor, configuration) == false) {
                return false;
            }
        }

        actor.changeEnergy(action.execute(actor, configuration));
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

    public Rule(List<Condition> conditions, Action action) {
        this.conditions = conditions;
        this.action = action;
    }

    public Rule() {
        this.conditions = new ArrayList<Condition>();
        this.conditions.add(new TrueCondition(new SpecifiedConditionTarget()));
        this.action = new IdleAction();
    }

    public String validate() {
        String msg = validateTargetsNotEmpty();
        return msg;
    }

    private String validateTargetsNotEmpty() {
        for (Condition c : conditions) {
            if (c instanceof TrueCondition) {
                continue;
            }
            if (c.getTarget() instanceof SingleTarget) {
                if (((SingleTarget)c.getTarget()).getDirection() == null) {
                    return "A condition has no target, please review your rule!";
                }
            } else if (((MultiTarget)c.getTarget()).getDirections().size() == 0) {
                return "A condition has no target, please review your rule!";
            }
        }
        if (action instanceof IdleAction) {
            return null;
        }
        if (action.getTarget() instanceof SingleTarget) {
            if (((SingleTarget)action.getTarget()).getDirection() == null) {
                return "An action has no target, please review your rule!";
            }
        } else if (((MultiTarget)action.getTarget()).getDirections().size() == 0) {
            return "An action has no target, please review your rule!";
        }
        return null;
    }
}
