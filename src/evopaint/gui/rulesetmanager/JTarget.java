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

import evopaint.pixel.rulebased.targeting.Target;
import evopaint.util.ImageRotator;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.IdentityHashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JTarget extends JPanel {

    private List<RelativeCoordinate> directions;
    private IdentityHashMap<JToggleButton,RelativeCoordinate> buttonsDirections;

    public List<RelativeCoordinate> createDirections() {
        return directions;
    }

    public JTarget(Target target, ActionListener buttonListener) {
        this(target);
        for (JToggleButton b : buttonsDirections.keySet()) {
            b.addActionListener(buttonListener);
        }
    }

    public JTarget(Target target) {
        this.directions = target.getDirections();
        setLayout(new GridLayout(3, 3, 4, 4));

        buttonsDirections = new IdentityHashMap<JToggleButton, RelativeCoordinate>();

        ImageIcon protoIconNorth = new ImageIcon(getClass().getResource("icons/north.png"));

        JToggleButton btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 315));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.NORTH_WEST);

        btn = new JToggleButton();
        btn.setIcon(protoIconNorth);
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.NORTH);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 45));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.NORTH_EAST);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 270));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.WEST);

        btn = new JToggleButton();
        btn.setIcon(new ImageIcon(getClass().getResource("icons/self.png")));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.SELF);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 90));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.EAST);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 225));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.SOUTH_WEST);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 180));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.SOUTH);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 135));
        add(btn);
        buttonsDirections.put(btn, RelativeCoordinate.SOUTH_EAST);

        for (JToggleButton b : buttonsDirections.keySet()) {
            b.setPreferredSize(new Dimension(30, 30));
            b.setMaximumSize(b.getPreferredSize());
            b.setMinimumSize(b.getPreferredSize());
            b.addActionListener(new TargetActionListener());
            for (RelativeCoordinate rc : directions) {
                if (rc == this.buttonsDirections.get(b)) {
                   b.setSelected(true);
                   break;
                }
            }
        }
    }

    private class TargetActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JToggleButton actionButton = (JToggleButton)e.getSource();
            RelativeCoordinate actionCoordinate = buttonsDirections.get(actionButton);
            if (actionButton.isSelected()) {
                if (directions.contains(actionCoordinate) == false) {
                    directions.add(actionCoordinate);
                }
            } else {
                directions.remove(actionCoordinate);
            }
        }
    }
}
