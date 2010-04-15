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

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
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
public class ChangeEnergyAction extends Action {

    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ChangeEnergyAction(int energyChange) {
        super(energyChange, null);
    }

    public ChangeEnergyAction() {
    }

    public String getName() {
        return "changeEnergyOf";
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        
        Pixel target = configuration.world.get(actor.getLocation(), direction);
        if (target == null) {
            return 0;
        }

        target.changeEnergy(amount);

        return energyChange;
    }

    @Override
    public Map<String, String>addParametersString(Map<String, String> parametersMap) {
        parametersMap = super.addParametersHTML(parametersMap);
        if (amount > 0) {
            parametersMap.put("target's reward", Integer.toString(amount));
        }
        else if (amount < 0) {
            parametersMap.put("target's cost", Integer.toString((-1) * amount));
        }
        return parametersMap;
    }

    @Override
    public Map<String, String>addParametersHTML(Map<String, String> parametersMap) {
        parametersMap = super.addParametersHTML(parametersMap);
        if (amount > 0) {
            parametersMap.put("target's reward", Integer.toString(amount));
        }
        else if (amount < 0) {
            parametersMap.put("target's cost", Integer.toString((-1) * amount));
        }
        return parametersMap;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(amount, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        JSpinner rewardValueSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        rewardValueSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setAmount(((Integer) ((JSpinner) e.getSource()).getValue()).byteValue());
            }
        });
        parametersMap.put("Target's Energy Change", rewardValueSpinner);

        return parametersMap;
    }

}
