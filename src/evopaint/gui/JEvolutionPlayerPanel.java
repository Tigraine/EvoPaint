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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JEvolutionPlayerPanel extends JPanel {

    public JEvolutionPlayerPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
        //setBorder(new LineBorder(Color.GRAY));
        //setBackground(new Color(0xF2F2F5));

        JButton recordButton = new JButton(new ImageIcon(getClass().getResource("icons/evolution-record.png")));
        recordButton.setPreferredSize(new Dimension(24, 24));
        recordButton.setContentAreaFilled(false);
        add(recordButton);

        JButton playButton = new JButton(new ImageIcon(getClass().getResource("icons/evolution-play.png")));
        playButton.setPreferredSize(new Dimension(24, 24));
        playButton.setContentAreaFilled(false);
        add(playButton);

        JToggleButton pauseButton = new JToggleButton(new ImageIcon(getClass().getResource("icons/evolution-pause.png")));
        pauseButton.setPressedIcon(new ImageIcon(getClass().getResource("icons/evolution-pause_pressed_inverted.png")));
        pauseButton.setRolloverIcon(new ImageIcon(getClass().getResource("icons/evolution-pause_hovered.png")));
        pauseButton.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-pause_pressed_inverted.png")));
        pauseButton.setSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-pause_pressed_inverted.png")));
        pauseButton.setPreferredSize(new Dimension(24, 24));
        pauseButton.setContentAreaFilled(false);
        add(pauseButton);

        JButton stopButton = new JButton(new ImageIcon(getClass().getResource("icons/evolution-stop.png")));
        stopButton.setPreferredSize(new Dimension(24, 24));
        stopButton.setContentAreaFilled(false);
        add(stopButton);

        JButton ejectButton = new JButton(new ImageIcon(getClass().getResource("icons/evolution-eject.png")));
        ejectButton.setPreferredSize(new Dimension(24, 24));
        ejectButton.setContentAreaFilled(false);
        add(ejectButton);
    }

}
