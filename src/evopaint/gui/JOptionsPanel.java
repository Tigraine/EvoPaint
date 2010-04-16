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

package evopaint.gui;

import evopaint.Configuration;
import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import evopaint.commands.SelectCommand;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JOptionsPanel extends JPanel {
    private Configuration configuration;

    public void displayOptions(Class toolClass) {
        if (toolClass == MoveCommand.class) {
            ((CardLayout)getLayout()).show(this, "empty");
            return;
        }

        if (toolClass == PaintCommand.class) {
            ((CardLayout)getLayout()).show(this, "paint");
            return;
        }

        if (toolClass == SelectCommand.class) {
            ((CardLayout)getLayout()).show(this, "empty");
            return;
        }
    }

    public JOptionsPanel(Configuration configuration) {
        this.configuration = configuration;
        setLayout(new CardLayout());

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(0xF2F2F5));
        add(emptyPanel, "empty");

        BrushOptionsPanel paintOptionsPanel = new BrushOptionsPanel(configuration);
        add(paintOptionsPanel, "paint");
    }
}
