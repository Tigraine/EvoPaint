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
import evopaint.gui.rulesetmanager.util.ColorChooserLabel;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class SetColorAction extends Action {

    private PixelColor color;

    public SetColorAction(int energyChange, ActionMetaTarget target, PixelColor color) {
        super(energyChange, target);
        this.color = color;
    }

    public SetColorAction() {
        this.color = new PixelColor(0);
    }

    public PixelColor getColor() {
        return color;
    }

    public void setColor(PixelColor color) {
        this.color = color;
    }
    
    public String getName() {
        return "set color";
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        Pixel target = configuration.world.get(actor.getLocation(), direction);
        if (target == null) {
            return 0;
        }
        target.setPixelColor(color.getHSB());
        return energyChange;
    }

    @Override
    public Map<String, String>addParametersString(Map<String, String> parametersMap) {
        parametersMap = super.addParametersString(parametersMap);
        parametersMap.put("color", color.toString());
        return parametersMap;
    }

    @Override
    public Map<String, String>addParametersHTML(Map<String, String> parametersMap) {
        parametersMap = super.addParametersHTML(parametersMap);
        parametersMap.put("color", color.toHTML());
        return parametersMap;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        ColorChooserLabel colorLabel = new ColorChooserLabel(color);
        JPanel wrapLabelToAvoidUncoloredStretchedBackground = new JPanel();
        wrapLabelToAvoidUncoloredStretchedBackground.add(colorLabel);
        parametersMap.put("Color", wrapLabelToAvoidUncoloredStretchedBackground);

        return parametersMap;
    }
}
