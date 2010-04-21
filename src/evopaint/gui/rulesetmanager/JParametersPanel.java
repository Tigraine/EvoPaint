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

package evopaint.gui.rulesetmanager;

import evopaint.pixel.rulebased.interfaces.IParameterized;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JParametersPanel extends JPanel {

    public JParametersPanel(final IParameterized parameterized) {
        setBorder(new TitledBorder("Parameters"));
        setLayout(new GridBagLayout());

        LinkedHashMap<String,JComponent> parametersMap = new LinkedHashMap<String, JComponent>();
        parametersMap = parameterized.addParametersGUI(parametersMap);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        for (String string : parametersMap.keySet()) {
            constraints.gridx = 0;
            constraints.insets = new Insets(0, 0, 5, 10);
            add(new JLabel(string + ":"), constraints);
            constraints.gridx = 1;
            constraints.insets = new Insets(0, 0, 5, 0);
            add(parametersMap.get(string), constraints);
            constraints.gridy = constraints.gridy + 1;
        }
    }
}
