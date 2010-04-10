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

package evopaint.gui.rulesetmanager.util;

import evopaint.pixel.ColorDimensions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class DimensionsListener implements ActionListener {

    ColorDimensions dimensions;
    private JToggleButton btnH;
    private JToggleButton btnS;
    private JToggleButton btnB;

    public DimensionsListener(ColorDimensions dimensions, JToggleButton btnH, JToggleButton btnS, JToggleButton btnB) {
        this.dimensions = dimensions;
        this.btnH = btnH;
        this.btnS = btnS;
        this.btnB = btnB;
    }

    public void actionPerformed(ActionEvent e) {
        JToggleButton source = (JToggleButton) e.getSource();

        if (source == this.btnH) {
            if (this.btnH.isSelected()) {
                dimensions.hue = true;
            } else {
                dimensions.hue = false;
            }
            return;
        }

        if (source == this.btnS) {
            if (this.btnS.isSelected()) {
                dimensions.saturation = true;
            } else {
                dimensions.saturation = false;
            }
            return;
        }

        if (source == this.btnB) {
            if (this.btnB.isSelected()) {
                dimensions.brightness = true;
            } else {
                dimensions.brightness = false;
            }
            return;
        }
    }
}
