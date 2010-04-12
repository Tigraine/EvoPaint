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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JEvolutionPlayerPanel extends JPanel {
    private Configuration configuration;

    public JEvolutionPlayerPanel(final Configuration configuration) {
        this.configuration = configuration;
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
        //setBorder(new LineBorder(Color.GRAY));
        //setBackground(new Color(0xF2F2F5));

        JToggleButton recordButton = new JToggleButton();
        recordButton.setRolloverEnabled(true);
        recordButton.setPreferredSize(new Dimension(24, 24));
        recordButton.setContentAreaFilled(false);
        recordButton.setIcon(new ImageIcon(getClass().getResource("icons/evolution-record.png")));
        recordButton.setRolloverIcon(new ImageIcon(getClass().getResource("icons/evolution-record-rollover.png")));
        recordButton.setPressedIcon(new ImageIcon(getClass().getResource("icons/evolution-record-pressed.png")));
        recordButton.setSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-record-selected.png")));
        recordButton.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-record-rollover-selected.png")));
        recordButton.setToolTipText("NOT WORKING YET: Records a video of your evolution");
        add(recordButton);

        JToggleButton playButton = new JToggleButton();
        playButton.setRolloverEnabled(true);
        playButton.setPreferredSize(new Dimension(24, 24));
        playButton.setContentAreaFilled(false);
        playButton.setIcon(new ImageIcon(getClass().getResource("icons/evolution-play.png")));
        playButton.setRolloverIcon(new ImageIcon(getClass().getResource("icons/evolution-play-rollover.png")));
        playButton.setPressedIcon(new ImageIcon(getClass().getResource("icons/evolution-play-pressed.png")));
        playButton.setSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-play-selected.png")));
        playButton.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-play-rollover-selected.png")));
        playButton.setToolTipText("Resumes the evolution");
        add(playButton);

        JToggleButton pauseButton = new JToggleButton(new ImageIcon(getClass().getResource("icons/evolution-pause.png")));
        pauseButton.setRolloverEnabled(true);
        pauseButton.setPreferredSize(new Dimension(24, 24));
        pauseButton.setContentAreaFilled(false);
        pauseButton.setIcon(new ImageIcon(getClass().getResource("icons/evolution-pause.png")));
        pauseButton.setRolloverIcon(new ImageIcon(getClass().getResource("icons/evolution-pause-rollover.png")));
        pauseButton.setPressedIcon(new ImageIcon(getClass().getResource("icons/evolution-pause-pressed.png")));
        pauseButton.setSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-pause-selected.png")));
        pauseButton.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-pause-rollover-selected.png")));
        pauseButton.setToolTipText("Pauses evolution, but keeps painting");
        add(pauseButton);

        JToggleButton stopButton = new JToggleButton(new ImageIcon(getClass().getResource("icons/evolution-stop.png")));
        stopButton.setRolloverEnabled(true);
        stopButton.setPreferredSize(new Dimension(24, 24));
        stopButton.setContentAreaFilled(false);
        stopButton.setIcon(new ImageIcon(getClass().getResource("icons/evolution-stop.png")));
        stopButton.setRolloverIcon(new ImageIcon(getClass().getResource("icons/evolution-stop-rollover.png")));
        stopButton.setPressedIcon(new ImageIcon(getClass().getResource("icons/evolution-stop-pressed.png")));
        stopButton.setSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-stop-selected.png")));
        stopButton.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("icons/evolution-stop-rollover-selected.png")));
        stopButton.setToolTipText("Pauses evolution and painting alltogether in case you need your CPU");
        add(stopButton);

        ButtonGroup group = new ButtonGroup();
        group.add(playButton);
        group.add(pauseButton);
        group.add(stopButton);

        JButton ejectButton = new JButton(new ImageIcon(getClass().getResource("icons/evolution-eject.png")));
        ejectButton.setRolloverEnabled(true);
        ejectButton.setPreferredSize(new Dimension(24, 24));
        ejectButton.setContentAreaFilled(false);
        ejectButton.setIcon(new ImageIcon(getClass().getResource("icons/evolution-eject.png")));
        ejectButton.setRolloverIcon(new ImageIcon(getClass().getResource("icons/evolution-eject-rollover.png")));
        ejectButton.setPressedIcon(new ImageIcon(getClass().getResource("icons/evolution-eject-pressed.png")));
        ejectButton.setToolTipText("NOT WORKING YET: Lets you choose to open/create an evolution or reset your current one");
        add(ejectButton);

        playButton.setSelected(true);

        playButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                configuration.runLevel = Configuration.RUNLEVEL_RUNNING;
            }
        });

        pauseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                configuration.runLevel = Configuration.RUNLEVEL_PAINTING_ONLY;
            }
        });

        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                configuration.runLevel = Configuration.RUNLEVEL_STOP;
            }
        });
    }

}
