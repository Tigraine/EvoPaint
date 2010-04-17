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
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.ActionTarget;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.util.ExceptionHandler;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public abstract class Action implements IHaveTarget, ICopyable {

    protected int energyChange;
    private IActionTarget target;

    protected Action(int energyChange, ActionMetaTarget target) {
        this.energyChange = energyChange;
        this.target = target;
    }

    protected Action() {
        this.target = new ActionTarget();
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

    public Action getCopy() {
        Action newAction = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newAction = (Action) in.readObject();
        } catch (ClassNotFoundException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
        return newAction;
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

    public int execute(Pixel actor, Configuration configuration) {
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

    public abstract int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration);

}
