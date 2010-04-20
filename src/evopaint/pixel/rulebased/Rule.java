/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.actions.ChangeEnergyAction;
import evopaint.pixel.rulebased.actions.CopyAction;
import evopaint.pixel.rulebased.actions.MoveAction;
import evopaint.pixel.rulebased.actions.PartnerProcreationAction;
import evopaint.pixel.rulebased.actions.SetColorAction;
import evopaint.pixel.rulebased.conditions.ColorLikenessColorCondition;
import evopaint.pixel.rulebased.conditions.ColorLikenessMyColorCondition;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.ExistenceCondition;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.MetaTarget;
import evopaint.pixel.rulebased.targeting.QualifiedMetaTarget;
import evopaint.pixel.rulebased.targeting.SingleTarget;
import evopaint.pixel.rulebased.targeting.qualifiers.ColorLikenessColorQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.ColorLikenessMyColorQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.EnergyQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.ExistenceQualifier;
import evopaint.pixel.rulebased.util.ObjectComparisonOperator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Rule implements IHTML {
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
        if (action instanceof MoveAction || action instanceof CopyAction) {
            ret += " to ";
        }
        else if (action instanceof ChangeEnergyAction || action instanceof SetColorAction) {
            ret += " of ";
        }
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

    public boolean apply(RuleBasedPixel actor, Configuration configuration) {
        for (Condition condition : conditions) {
            if (condition.isMet(actor, configuration) == false) {
                return false;
            }
        }

        actor.changeEnergy(action.execute(actor, configuration));
        return true;
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

    public Rule(Rule rule) {
        this.conditions = new ArrayList(rule.conditions);
        this.action = rule.action;
    }

    public String validate() {
        String msg = null;
        if ((msg = validateTargetsNotEmpty()) != null) {
            return msg;
        }
        if ((msg = validateQualifiers()) != null) {
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

    private String validateQualifiers() {
        if (false == action.getTarget() instanceof QualifiedMetaTarget) {
            return null;
        }

        List<Qualifier> qualifiers = ((QualifiedMetaTarget)action.getTarget()).getQualifiers();
        
        boolean foundIsPixel = false;
        boolean foundIsFreeSpot = false;
        boolean foundHasLeastEnergy = false;
        boolean foundHasMostEnergy = false;
        boolean foundHasColorLeastLikeColor = false;
        boolean foundHasColorMostLikeColor = false;
        boolean foundHasColorLeastLikeMe = false;
        boolean foundHasColorMostLikeMe = false;

        ArrayList<Qualifier> seen = new ArrayList<Qualifier>();
        for (Qualifier q : qualifiers) {

            // check for doublicates
            for (Qualifier seenQ : seen) {
                if (seenQ.equals(q)) {
                    return "You have doublicate action target qualifiers.\nThis makes no sense, but will influence performance, so please review your rule!";
                }
            }
            seen.add(q);

            // gather information about existence of qualifiers
            if (q instanceof ExistenceQualifier) {
                if (((ExistenceQualifier)q).getObjectComparisonOperator() ==
                        ObjectComparisonOperator.EQUAL) {
                    foundIsPixel = true;
                }
                else {
                    foundIsFreeSpot = true;
                }
            }
            else if (q instanceof EnergyQualifier) {
                if (((EnergyQualifier)q).isLeast()) {
                    foundHasLeastEnergy = true;
                }
                else {
                    foundHasMostEnergy = true;
                }
            }
            else if (q instanceof ColorLikenessColorQualifier) {
                if (((ColorLikenessColorQualifier)q).isLeast()) {
                    foundHasColorLeastLikeColor = true;
                }
                else {
                    foundHasColorMostLikeColor = true;
                }
            }
            else if (q instanceof ColorLikenessMyColorQualifier) {
                if (((ColorLikenessMyColorQualifier)q).isLeast()) {
                    foundHasColorLeastLikeMe = true;
                }
                else {
                    foundHasColorMostLikeMe = true;
                }
            }
        }

        if (foundIsPixel) {

            // check for most obvious conflict
            if (foundIsFreeSpot) {
                return "Are you female? Just asking, because you want your target to be existent and non existent at the same time.\nHow about we fix that before we continue, shall we?";
            }

            // check for redundancy
            if (foundHasLeastEnergy || foundHasMostEnergy ||
                    foundHasColorLeastLikeColor || foundHasColorMostLikeColor ||
                    foundHasColorLeastLikeMe || foundHasColorMostLikeMe) {
                 return "You have redundant action target qualifiers.\nAll qualifiers except for the Non-Existence qualifier will check if their target is existent,\nso you can safely remove the Existence qualifier, which will improve performance.";
            }
        }

        // check for other conflicts
        if (foundIsFreeSpot) {
            if (foundHasLeastEnergy || foundHasMostEnergy ||
                    foundHasColorLeastLikeColor || foundHasColorMostLikeColor ||
                    foundHasColorLeastLikeMe || foundHasColorMostLikeMe) {
                return "How can a non-existing pixel have any other attributes to check for? Sense much?\nGo fix that before I download gay porn onto your hard disc and screw up your OS\nso the guys at the computer store can have a good laugh at your expense!";
            }
        }
        if (foundHasLeastEnergy && foundHasMostEnergy) {
            return "The one with the least energy which has the most energy, hu?\nFix that before I get really mad at you for even trying!";
        }
        // least like green, most like red will favor blue over green
        // the only case where we would want to catch this is when
        // the colors are the same. but this means creating a second equals()
        // which would suck
        // if (foundHasColorLeastLikeColor && foundHasColorMostLikeColor) {
        // }
        if (foundHasColorLeastLikeMe && foundHasColorMostLikeMe) {
            return "The one whose color is least and most like me at the same time, hu?\nI hate you!";
        }

        return null;
    }

    public void mixWith(Rule theirRule, float theirShare, IRandomNumberGenerator rng) {
        // conditions
        // cache size() calls for maximum performance
        int ourSize = conditions.size();
        int theirSize = theirRule.conditions.size();

        // now mix as many conditions as we have in common and add the rest depending
        // on share percentage
        // we have more conditions
        if (ourSize > theirSize) {
            int i = 0;
            while (i < theirSize) {
                Condition ourCondition = conditions.get(i);
                Condition theirCondition = theirRule.conditions.get(i);
                if (ourCondition.getType() == theirCondition.getType()) {
                    Condition newCondition = null;
                    int type = ourCondition.getType();
                    switch (type) {
                        case Condition.COLOR_LIKENESS_COLOR:
                            newCondition = new ColorLikenessColorCondition(
                                    (ColorLikenessColorCondition)theirCondition);
                            break;
                        case Condition.COLOR_LIKENESS_MY_COLOR:
                            newCondition = new ColorLikenessMyColorCondition(
                                    (ColorLikenessMyColorCondition)theirCondition);
                            break;
                        case Condition.ENERGY:
                            newCondition = new EnergyCondition(
                                    (EnergyCondition)theirCondition);
                            break;
                        case Condition.EXISTENCE:
                            newCondition = new ExistenceCondition(
                                    (ExistenceCondition)theirCondition);
                        case Condition.TRUE:
                            newCondition = theirCondition;
                            break;
                        default: assert (false);
                    }
                    newCondition.mixWith(theirCondition, theirShare, rng);
                    conditions.set(i, newCondition);
                } else {
                    if (rng.nextFloat() < theirShare) {
                        conditions.set(i, theirCondition);
                    }
                }
                i++;
            }
            int removed = 0;
            while (i < ourSize - removed) {
                if (rng.nextFloat() < theirShare) {
                    conditions.remove(i);
                    removed ++;
                } else {
                    i++;
                }
            }
        } else { // they have more conditions or we have an equal number of conditions
           int i = 0;
            while (i < ourSize) {
                Condition ourCondition = conditions.get(i);
                Condition theirCondition = theirRule.conditions.get(i);
                if (ourCondition.getType() == theirCondition.getType()) {
                    Condition newCondition = null;
                    int type = ourCondition.getType();
                    switch (type) {
                        case Condition.COLOR_LIKENESS_COLOR:
                            newCondition = new ColorLikenessColorCondition(
                                    (ColorLikenessColorCondition)theirCondition);
                            break;
                        case Condition.COLOR_LIKENESS_MY_COLOR:
                            newCondition = new ColorLikenessMyColorCondition(
                                    (ColorLikenessMyColorCondition)theirCondition);
                            break;
                        case Condition.ENERGY:
                            newCondition = new EnergyCondition(
                                    (EnergyCondition)theirCondition);
                            break;
                        case Condition.EXISTENCE:
                            newCondition = new ExistenceCondition(
                                    (ExistenceCondition)theirCondition);
                        case Condition.TRUE:
                            newCondition = theirCondition;
                            break;
                        default: assert (false);
                    }
                    newCondition.mixWith(theirCondition, theirShare, rng);
                    conditions.set(i, newCondition);
                } else {
                    if (rng.nextFloat() < theirShare) {
                        conditions.set(i, theirCondition);
                    }
                }
                i++;
            }
            while (i < theirSize) {
                if (rng.nextFloat() < theirShare) {
                    conditions.add(theirRule.conditions.get(i));
                }
                i++;
            }
        }

        if (action.getType() == theirRule.action.getType()) {
            int type = action.getType();
            switch (type) {
                case Action.ASSIMILATION:
                    action = new AssimilationAction(
                            (AssimilationAction)theirRule.action);
                    break;
                case Action.CHANGE_ENERGY:
                    action = new ChangeEnergyAction(
                            (ChangeEnergyAction)theirRule.action);
                    break;
                case Action.COPY:
                    action = new CopyAction(
                            (CopyAction)theirRule.action);
                    break;
                case Action.MOVE:
                    action = new MoveAction(
                            (MoveAction)theirRule.action);
                    break;
                case Action.PARTNER_PROCREATION:
                    action = new PartnerProcreationAction(
                            (PartnerProcreationAction)theirRule.action);
                    break;
                case Action.SET_COLOR:
                    action = new SetColorAction(
                            (SetColorAction)theirRule.action);
                    break;
                default: assert (false);
            }
            action.mixWith(theirRule.action, theirShare, rng);
        } else {
            if (rng.nextFloat() < theirShare) {
                action = theirRule.action;
            }
        }
    }

}
