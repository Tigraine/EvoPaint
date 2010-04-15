/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.actions.ChangeEnergyAction;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.MetaTarget;
import evopaint.pixel.rulebased.targeting.QualifiedMetaTarget;
import evopaint.pixel.rulebased.targeting.SingleTarget;
import evopaint.pixel.rulebased.targeting.qualifiers.ExistenceQualifier;
import evopaint.pixel.rulebased.util.ObjectComparisonOperator;
import evopaint.util.ExceptionHandler;
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
        String ret = "<span style='color: #0000E6; font-weight: bold;'>if</span> ";
        for (Iterator<Condition> ii = conditions.iterator(); ii.hasNext();) {
            Condition condition = ii.next();
            ret += condition.toHTML();
            if (ii.hasNext()) {
                ret += " <span style='color: #0000E6; font-weight: bold;'>and</span> ";
            }
        }
        ret += " <span style='color: #0000E6; font-weight: bold;'>then</span> ";
        
        ret += action.toHTML();
        ret += " ";
        ITarget target = action.getTarget();
        ret += action.getTarget().toHTML();
        if (target instanceof ActionMetaTarget) {
            ret += " <span style='color: #0000E6; font-weight: bold;'>which</span> ";
            List<Qualifier> qualifiers = ((ActionMetaTarget)target).getQualifiers();
            for (Iterator<Qualifier> ii = qualifiers.iterator(); ii.hasNext();) {
                ret += ii.next().toHTML();
                if (ii.hasNext()) {
                    ret += " <span style='color: #0000E6; font-weight: bold;'>and</span> ";
                }
            }
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
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
        return newRule;
    }

    public Rule(List<Condition> conditions, Action action) {
        this.conditions = conditions;
        this.action = action;
    }

    public Rule() {
        this.conditions = new ArrayList<Condition>();
        this.conditions.add(new TrueCondition());
        this.action = new ChangeEnergyAction();
    }

    public String validate() {
        String msg = null;
        if ((msg = validateTargetsNotEmpty()) != null) {
            return msg;
        }
        if ((msg = validateQualifiersNotDouble()) != null) {
            return msg;
        }
        if ((msg = validateQualifiersNotRedundant()) != null) {
            return msg;
        }
        if ((msg = validateQualifiersNotConflicting()) != null) {
            return msg;
        }
        return null;
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
            } else if (((MetaTarget)c.getTarget()).getDirections().size() == 0) {
                return "A condition has no target, please review your rule!";
            }
        }
        if (action.getTarget() instanceof SingleTarget) {
            if (((SingleTarget)action.getTarget()).getDirection() == null) {
                return "The action has no target, please review your rule!";
            }
        } else if (((MetaTarget)action.getTarget()).getDirections().size() == 0) {
            return "The action has no target, please review your rule!";
        }
        return null;
    }

    private String validateQualifiersNotDouble() {
        if (false == action.getTarget() instanceof QualifiedMetaTarget) {
            return null;
        }
        ArrayList<Qualifier> seen = new ArrayList<Qualifier>();
        for (Qualifier q : ((QualifiedMetaTarget)action.getTarget()).getQualifiers()) {
            if (seen.contains(q)) { // qualifiers are never created anew
                return "You have doublicate action target qualifiers.\nThis makes no sense, but will influence performance, so please review your rule!";
            }
            seen.add(q);
        }
        return null;
    }

    private String validateQualifiersNotRedundant() {
        if (false == action.getTarget() instanceof QualifiedMetaTarget) {
            return null;
        }
        List<Qualifier> qualifiers = ((QualifiedMetaTarget)action.getTarget()).getQualifiers();
        for (Qualifier q : qualifiers) {
         //   if (false == (q instanceof ExistenceQualifier ||
    //                ((ExistenceQualifier)q).getObjectComparisonOperator() ==
    //                ObjectComparisonOperator.NOT_EQUAL)) {
    //            for
   //         }
    //            if (qualifiers.contains(ExistenceQualifier.getInstance())) {
        //            return "You have redundant action target qualifiers.\nAll qualifiers except for the Non-Existence qualifier will check if their target is existent,\nso you can safely remove the Existence qualifier, which will improve performance.";
        //        }
        //    }
        }
        return null;
    }

    private String validateQualifiersNotConflicting() {
        if (false == action.getTarget() instanceof QualifiedMetaTarget) {
            return null;
        }
        List<Qualifier> qualifiers = ((QualifiedMetaTarget)action.getTarget()).getQualifiers();
        for (Qualifier q : qualifiers) {
            /*if (q instanceof NonExistenceQualifier) {
            if (qualifiers.size() > 1) {
            return "How can a non-existing pixel have any other attributes to check for? Sense much?\nGo fix that before I download gay porn onto your hard disc and screw up your OS\nso the guys at the computer store can have a good laugh at your expense!";
            }
            }
            if (q instanceof LeastEnergyQualifier) {
            if (qualifiers.contains(MostEnergyQualifier.getInstance())) {
            return "The one with the least energy which has the most energy, hu?\nFix that before I get really mad at you for even trying!";
            }
            }*/
        }
        return null;
    }
}
