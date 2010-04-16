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
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import java.util.Map;
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
public class PartnerProcreationAction extends Action {
    private int partnerEnergyChange;
    private ColorDimensions dimensions;
    private byte ourSharePercent;

    public PartnerProcreationAction(int energyChange, int partnerEnergyChange, ActionMetaTarget partner, ColorDimensions dimensions, byte ourSharePercent) {
        super(energyChange, partner);
        this.partnerEnergyChange = partnerEnergyChange;
        this.dimensions = dimensions;
        this.ourSharePercent = ourSharePercent;
    }

    public PartnerProcreationAction() {
        this.dimensions = new ColorDimensions(true, true, true);
        ourSharePercent = 50;
    }

    public ColorDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(ColorDimensions dimensionsToMix) {
        this.dimensions = dimensionsToMix;
    }

    public byte getOurSharePercent() {
        return ourSharePercent;
    }

    public void setOurSharePercent(byte ourSharePercent) {
        this.ourSharePercent = ourSharePercent;
    }

    public String getName() {
        return "procreate with partner";
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        Pixel partner = configuration.world.get(actor.getLocation(), direction);
        if (partner == null) {
            return 0;
        }

        AbsoluteCoordinate randomFreeSpot =
                configuration.world.getRandomFreeNeighborCoordinateOf(
                actor.getLocation(), configuration.rng);

        if (randomFreeSpot == null) {
            return 0;
        }

        PixelColor newPixelColor = new PixelColor(partner.getPixelColor());
        newPixelColor.mixWith(actor.getPixelColor(), ((float)ourSharePercent) / 100, dimensions);

        // TODO really mix these rule sets
        RuleSet newRuleSet = configuration.rng.nextBoolean() ? ((RuleBasedPixel)actor).getRuleSet() : ((RuleBasedPixel)partner).getRuleSet();

        Pixel newPixel = new RuleBasedPixel(
                newPixelColor,
                randomFreeSpot,
                (actor.getEnergy() + getEnergyChange() + partner.getEnergy() + partnerEnergyChange) / 2,
                newRuleSet);

        configuration.world.set(newPixel);

        partner.changeEnergy(partnerEnergyChange);

        return energyChange;
    }

    @Override
    public Map<String, String>addParametersString(Map<String, String> parametersMap) {
        parametersMap = super.addParametersString(parametersMap);
        if (partnerEnergyChange > 0) {
            parametersMap.put("partner's reward", Integer.toString(partnerEnergyChange));
        }
        else if (partnerEnergyChange < 0) {
            parametersMap.put("partner's cost", Integer.toString((-1) * partnerEnergyChange));
        }
        parametersMap.put("dimensions", dimensions.toString());
        parametersMap.put("our share in %", Integer.toString(ourSharePercent));
        return parametersMap;
    }

    @Override
    public Map<String, String>addParametersHTML(Map<String, String> parametersMap) {
        parametersMap = super.addParametersHTML(parametersMap);
        if (partnerEnergyChange > 0) {
            parametersMap.put("partner's reward", Integer.toString(partnerEnergyChange));
        }
        else if (partnerEnergyChange < 0) {
            parametersMap.put("partner's cost", Integer.toString((-1) * partnerEnergyChange));
        }
        parametersMap.put("dimensions", dimensions.toHTML());
        parametersMap.put("our share in %", Integer.toString(ourSharePercent));
        return parametersMap;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        SpinnerNumberModel partnerEnergyModel = new SpinnerNumberModel(partnerEnergyChange, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        JSpinner partnerEnergyChangeSpinner = new AutoSelectOnFocusSpinner(partnerEnergyModel);
        partnerEnergyChangeSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                partnerEnergyChange = (Integer) ((JSpinner) e.getSource()).getValue();
            }
        });
        parametersMap.put("Partner's Energy Change", partnerEnergyChangeSpinner);

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
                new SpinnerNumberModel(ourSharePercent, 0, 100, 1);
        JSpinner ourSharePercentSpinner =
                new AutoSelectOnFocusSpinner(ourSharePercentSpinnerModel);
        ourSharePercentSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ourSharePercent =
                        ((Integer)((JSpinner)e.getSource()).getValue()).byteValue();
            }
        });
        parametersMap.put("Our share in %", ourSharePercentSpinner);

        return parametersMap;
    }
}
