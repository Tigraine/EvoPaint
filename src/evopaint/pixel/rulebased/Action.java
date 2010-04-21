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

import evopaint.pixel.rulebased.targeting.IHaveTarget;
import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.actions.ChangeEnergyAction;
import evopaint.pixel.rulebased.actions.CopyAction;
import evopaint.pixel.rulebased.actions.MoveAction;
import evopaint.pixel.rulebased.actions.PartnerProcreationAction;
import evopaint.pixel.rulebased.actions.SetColorAction;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.ActionSingleTarget;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.Target;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public abstract class Action implements IHaveTarget {

    protected final static int ASSIMILATION = 0;
    protected final static int CHANGE_ENERGY = 1;
    protected final static int COPY = 2;
    protected final static int MOVE = 3;
    protected final static int PARTNER_PROCREATION = 4;
    protected final static int SET_COLOR = 5;

    private static final int NUM_ACTIONS = 6;

    protected int energyChange;
    private IActionTarget target;

    protected Action(int energyChange, ActionMetaTarget target) {
        this.energyChange = energyChange;
        this.target = target;
    }

    protected Action() {
        this.target = new ActionSingleTarget();
    }

    protected Action(Action action) {
        this.energyChange = action.energyChange;
        this.target = action.target;
    }

    public static Action copy(Action action) {
        int type = action.getType();
        switch (type) {
            case Action.ASSIMILATION:
                return new AssimilationAction(
                        (AssimilationAction)action);
            case Action.CHANGE_ENERGY:
                return new ChangeEnergyAction(
                        (ChangeEnergyAction)action);
            case Action.COPY:
                return new CopyAction(
                        (CopyAction)action);
            case Action.MOVE:
                return new MoveAction(
                        (MoveAction)action);
            case Action.PARTNER_PROCREATION:
                return new PartnerProcreationAction(
                        (PartnerProcreationAction)action);
            case Action.SET_COLOR:
                return new SetColorAction(
                        (SetColorAction)action);
            default: assert (false);
                return null;
        }
    }

    public abstract int getType();

    public int countGenes() {
        return target.countGenes(); // energy change is not mutable, so not counted
    }

    public void mutate(int mutatedGene, IRandomNumberGenerator rng) {
        int targetType = target.getType();
        switch (targetType) {
            case Target.META_TARGET: target = new ActionMetaTarget((ActionMetaTarget)target);
            break;
            case Target.SINGLE_TARGET: target = new ActionSingleTarget((ActionSingleTarget)target);
            break;
            default: assert (false);
        }
        target.mutate(mutatedGene, rng);
    }

    public void mixWith(Action theirAction, float theirShare, IRandomNumberGenerator rng) {
        if (target.getType() == theirAction.target.getType()) {
            Target newTarget = null;
            int targetType = target.getType();
            switch (targetType) {
                case Target.META_TARGET: newTarget = new ActionMetaTarget((ActionMetaTarget)target);
                break;
                case Target.SINGLE_TARGET: newTarget = new ActionSingleTarget((ActionSingleTarget)target);
                break;
                default: assert (false);
            }
            newTarget.mixWith((Target)theirAction.target, theirShare, rng);
            target = (IActionTarget)newTarget;
        } else {
            if (rng.nextFloat() < theirShare) {
                target = theirAction.target;
            }
        }
    }

    public int getEnergyChange() {
        return energyChange;
    }

    public void setEnergyChange(int energyChange) {
        this.energyChange = energyChange;
    }

    public IActionTarget getTarget() {
        return target;
    }

    public void setTarget(ITarget target) {
        this.target = (IActionTarget)target;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += getName();
        ret += " (";
        Map<String, String> parametersMap = addParametersString(new LinkedHashMap<String, String>());
        for (Iterator<String> ii = parametersMap.keySet().iterator(); ii.hasNext();) {
            String parameterName = ii.next();
            ret += parameterName + ": " + parametersMap.get(parameterName);
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += ")";

        return ret;
    }

    public String toHTML() {
        String ret = new String();
        ret += "<b>" + getName() + "</b>";
        ret += "(";
        Map<String, String> parametersMap = addParametersHTML(new LinkedHashMap<String, String>());
        for (Iterator<String> ii = parametersMap.keySet().iterator(); ii.hasNext();) {
            String parameterName = ii.next();
            ret += "<span style='color: #777777;'>" + parameterName + ":</span> " +
                    parametersMap.get(parameterName);
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += ")";

        return ret;
    }

    public int execute(RuleBasedPixel actor, Configuration configuration) {
        if (actor.getEnergy() + energyChange < 0) { // die trying
            return energyChange;
        }
        return target.execute(this, actor, configuration);
    }
    
    public LinkedHashMap<String, JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(energyChange, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        JSpinner energyChangeSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        //energyChangeSpinner.setEditor(new JSpinner.NumberEditor(energyChangeSpinner, "+#;-#"));
        energyChangeSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                energyChange = (Integer)((JSpinner)e.getSource()).getValue();
            }
        });
        parametersMap.put("My Energy Change", energyChangeSpinner);

        return parametersMap;
    }

    public Map<String, String> addParametersHTML(Map<String, String> parametersMap) {
        if (energyChange > 0) {
            parametersMap.put("my reward", Integer.toString(energyChange));
        }
        else if (energyChange < 0) {
            parametersMap.put("my cost", Integer.toString((-1) * energyChange));
        }
        return parametersMap;
    }

    public Map<String, String> addParametersString(Map<String, String> parametersMap) {
        if (energyChange > 0) {
            parametersMap.put("my reward", Integer.toString(energyChange));
        }
        else if (energyChange < 0) {
            parametersMap.put("my cost", Integer.toString((-1) * energyChange));
        }
        return parametersMap;
    }

    public abstract int execute(RuleBasedPixel actor, RelativeCoordinate direction, Configuration configuration);

}
