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

package evopaint.pixel.rulebased.targeting.qualifiers;

import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.ColorChooserLabel;
import evopaint.gui.rulesetmanager.util.DimensionsListener;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ColorLikenessQualifierColor extends Qualifier {

    private boolean isLeast;
    private PixelColor comparedColor;
    private ColorDimensions dimensions;

    public ColorLikenessQualifierColor(boolean isLeast, PixelColor comparedColor, ColorDimensions dimensions) {
        this.isLeast = isLeast;
        this.comparedColor = comparedColor;
        this.dimensions = dimensions;
    }

    public ColorLikenessQualifierColor() {
        this.comparedColor = new PixelColor(0);
        this.dimensions = new ColorDimensions(true, true, true);
    }

    public boolean isLeast() {
        return isLeast;
    }

    public void setLeast(boolean isLeast) {
        this.isLeast = isLeast;
    }

    public PixelColor getColor() {
        return comparedColor;
    }

    public void setColor(PixelColor color) {
        this.comparedColor = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColorLikenessQualifierColor other = (ColorLikenessQualifierColor) obj;
        if (this.isLeast != other.isLeast) {
            return false;
        }
        if (this.comparedColor != other.comparedColor && (this.comparedColor == null || !this.comparedColor.equals(other.comparedColor))) {
            return false;
        }
        if (this.dimensions != other.dimensions && (this.dimensions == null || !this.dimensions.equals(other.dimensions))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.isLeast ? 1 : 0);
        hash = 59 * hash + (this.comparedColor != null ? this.comparedColor.hashCode() : 0);
        hash = 59 * hash + (this.dimensions != null ? this.dimensions.hashCode() : 0);
        return hash;
    }

    public String getName() {
        return "color likeness (color)";
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += "is colored the ";
        if (isLeast) {
            ret += "least";
        } else {
            ret += "most";
        }
        ret += " like ";
        ret += comparedColor.toString();
        ret += " (dimensions: ";
        ret += dimensions.toString();
        ret += ")";
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = new String();
        ret += "is colored the ";
        if (isLeast) {
            ret += "least";
        } else {
            ret += "most";
        }
        ret += " like ";
        ret += comparedColor.toHTML();
        ret += " <span style='color: #777777;'>(dimensions: ";
        ret += dimensions.toHTML();
        ret += ")</span>";
        return ret;
    }

    public List<RelativeCoordinate> getCandidates(RuleBasedPixel origin, List<RelativeCoordinate> directions, Configuration configuration) {
        List<RelativeCoordinate> ret = new ArrayList(1);
        if (isLeast) {
            double maxDistance = 0d;
            for (RelativeCoordinate direction : directions) {
                RuleBasedPixel target = configuration.world.get(origin.getLocation(), direction);
                if (target == null) {
                    continue;
                }
                double distance = target.getPixelColor().distanceTo(comparedColor, dimensions);
                if (distance > maxDistance) {
                    ret.clear();
                    maxDistance = distance;
                    ret.add(direction);
                } else if (distance == maxDistance) {
                    ret.add(direction);
                }
            }
        } else {
            double minDistance = Double.MAX_VALUE;
            for (RelativeCoordinate direction : directions) {
                RuleBasedPixel target = configuration.world.get(origin.getLocation(), direction);
                if (target == null) {
                    continue;
                }
                double distance = target.getPixelColor().distanceTo(comparedColor, dimensions);
                if (distance < minDistance) {
                    ret.clear();
                    minDistance = distance;
                    ret.add(direction);
                } else if (distance == minDistance) {
                    ret.add(direction);
                }
            }
        }
        return ret;
    }

    @Override
    public LinkedHashMap<String, JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        JComboBox comparisonComboBox = new JComboBox();
        comparisonComboBox.setModel(new DefaultComboBoxModel(new String [] {"the least", "the most"}));
        comparisonComboBox.setSelectedItem(isLeast ? "the least" : "the most");
        comparisonComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String chosen = (String)((JComboBox) (e.getSource())).getSelectedItem();
                if (chosen.equals("the least")) {
                    setLeast(true);
                } else {
                    setLeast(false);
                }
            }
        });
        parametersMap.put("Comparison", comparisonComboBox);

        ColorChooserLabel colorLabel = new ColorChooserLabel(comparedColor);
        JPanel wrapLabelToAvoidUncoloredStretchedBackground = new JPanel();
        wrapLabelToAvoidUncoloredStretchedBackground.add(colorLabel);
        parametersMap.put("Color", wrapLabelToAvoidUncoloredStretchedBackground);

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

        return parametersMap;
    }

}
