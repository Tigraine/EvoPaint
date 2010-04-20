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
import evopaint.gui.rulesetmanager.util.DimensionsListener;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.rulebased.Action;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class AssimilationAction extends Action {

    private ColorDimensions dimensions;
    private float ourShare;
    private boolean mixRules;

    public AssimilationAction(int energyChange, ActionMetaTarget target, ColorDimensions dimensions, float ourShare, boolean mixRuleSet) {
        super(energyChange, target);
        this.dimensions = dimensions;
        this.ourShare = ourShare;
        this.mixRules = mixRuleSet;
    }

    public AssimilationAction() {
        this.dimensions = new ColorDimensions(true, true, true);
        this.ourShare = 0.5f;
        this.mixRules = true;
    }

    public AssimilationAction(AssimilationAction assimilationAction) {
        super(assimilationAction);
        this.dimensions = new ColorDimensions(assimilationAction.dimensions);
        this.ourShare = assimilationAction.ourShare;
        this.mixRules = assimilationAction.mixRules;
    }

    public int getType() {
        return Action.ASSIMILATION;
    }

    @Override
    public void mixWith(Action theirAction, float theirShare, IRandomNumberGenerator rng) {
        super.mixWith(theirAction, theirShare, rng);
        AssimilationAction a = (AssimilationAction)theirAction;
        dimensions.mixWith(a.dimensions, theirShare, rng);
        if (rng.nextFloat() < theirShare) {
            ourShare = a.ourShare;
        }
    }
    
    public ColorDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(ColorDimensions dimensionsToMix) {
        this.dimensions = dimensionsToMix;
    }

    public float getOurShare() {
        return ourShare;
    }

    public void setOurShare(float ourShare) {
        this.ourShare = ourShare;
    }


    public String getName() {
        return "assimilate";
    }

    public int execute(RuleBasedPixel actor, RelativeCoordinate direction, Configuration configuration) {
        RuleBasedPixel target = configuration.world.get(actor.getLocation(), direction);
        if (target == null) {
            return 0;
        }

        if (mixRules) {
            RuleBasedPixel newPixel = new RuleBasedPixel(target);
            newPixel.mixWith(actor, ourShare, configuration.rng);
            configuration.world.set(newPixel);
        }
        else {
            PixelColor newColor = new PixelColor(target.getPixelColor());
            newColor.mixWith(actor.getPixelColor(), ourShare, dimensions);
            target.setPixelColor(newColor);
        }
                
        return energyChange;
    }

    @Override
    public Map<String, String>addParametersString(Map<String, String> parametersMap) {
        parametersMap = super.addParametersString(parametersMap);
        parametersMap.put("dimensions", dimensions.toString());
        parametersMap.put("our share in %", Integer.toString(Math.round(ourShare * 100)));
        parametersMap.put("mode", "color " + (mixRules ? "and rules" : "only"));
        return parametersMap;
    }

    @Override
    public Map<String, String>addParametersHTML(Map<String, String> parametersMap) {
        parametersMap = super.addParametersHTML(parametersMap);
        parametersMap.put("dimensions", dimensions.toHTML());
        parametersMap.put("our share in %", Integer.toString(Math.round(ourShare * 100)));
        parametersMap.put("mode", "color " + (mixRules ? "and rules" : "only"));
        return parametersMap;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        JPanel dimensionsPanel = new JPanel();
        JToggleButton btnH = new JToggleButton("H");
        JToggleButton btnS = new JToggleButton("S");
        JToggleButton btnB = new JToggleButton("B");
        DimensionsListener dimensionsListener = new DimensionsListener(dimensions, btnH, btnS, btnB);
        btnH.addActionListener(dimensionsListener);
        btnS.addActionListener(dimensionsListener);
        btnB.addActionListener(dimensionsListener);
        if (dimensions.hue) {
            btnH.setSelected(true);
        }
        if (dimensions.saturation) {
            btnS.setSelected(true);
        }
        if (dimensions.brightness) {
            btnB.setSelected(true);
        }
        dimensionsPanel.add(btnH);
        dimensionsPanel.add(btnS);
        dimensionsPanel.add(btnB);
        parametersMap.put("Dimensions", dimensionsPanel);

        SpinnerNumberModel ourSharePercentSpinnerModel =
                new SpinnerNumberModel(Math.round(ourShare * 100), 0, 100, 1);
        JSpinner ourSharePercentSpinner =
                new AutoSelectOnFocusSpinner(ourSharePercentSpinnerModel);
        ourSharePercentSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ourShare =
                        ((Integer)((JSpinner)e.getSource()).getValue()).floatValue() / 100;
            }
        });
        parametersMap.put("Our share in %", ourSharePercentSpinner);


        final JCheckBox mixRuleSetCheckBox = new JCheckBox();
        mixRuleSetCheckBox.setSelected(mixRules);
        mixRuleSetCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mixRules = mixRuleSetCheckBox.isSelected();
            }
        });
        parametersMap.put("Mix rules", mixRuleSetCheckBox);

        return parametersMap;
    }
}
