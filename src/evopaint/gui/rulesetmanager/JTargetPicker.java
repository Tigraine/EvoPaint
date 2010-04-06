/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

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
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JTargetPicker extends JPanel {

    private List<RelativeCoordinate> directions;
    private IdentityHashMap<JToggleButton,RelativeCoordinate> targets;

    public void setDirections(List<RelativeCoordinate> directions) {
        this.directions = directions;

        for (JToggleButton b : targets.keySet()) {
            b.setSelected(false);
            for (RelativeCoordinate rc : directions) {
                if (rc == this.targets.get(b)) {
                   b.setSelected(true);
                   break;
                }
            }
        }
    }

    public JTargetPicker(ActionListener maxRangeListener) {
        this();
        for (JToggleButton b : targets.keySet()) {
            b.addActionListener(maxRangeListener);
        }
    }

    public JTargetPicker() {
        setLayout(new GridLayout(3, 3, 4, 4));
        //setBorder(new TitledBorder("Targets"));

        targets = new IdentityHashMap<JToggleButton, RelativeCoordinate>();

        ImageIcon protoIconNorth = new ImageIcon(getClass().getResource("icons/north.png"));

        JToggleButton btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 315));
        add(btn);
        targets.put(btn, RelativeCoordinate.NORTH_WEST);

        btn = new JToggleButton();
        btn.setIcon(protoIconNorth);
        add(btn);
        targets.put(btn, RelativeCoordinate.NORTH);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 45));
        add(btn);
        targets.put(btn, RelativeCoordinate.NORTH_EAST);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 270));
        add(btn);
        targets.put(btn, RelativeCoordinate.WEST);

        btn = new JToggleButton();
        btn.setIcon(new ImageIcon(getClass().getResource("icons/self.png")));
        add(btn);
        targets.put(btn, RelativeCoordinate.SELF);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 90));
        add(btn);
        targets.put(btn, RelativeCoordinate.EAST);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 225));
        add(btn);
        targets.put(btn, RelativeCoordinate.SOUTH_WEST);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 180));
        add(btn);
        targets.put(btn, RelativeCoordinate.SOUTH);

        btn = new JToggleButton();
        btn.setIcon(ImageRotator.createRotatedImage(btn, protoIconNorth, 135));
        add(btn);
        targets.put(btn, RelativeCoordinate.SOUTH_EAST);

        for (JToggleButton b : targets.keySet()) {
            b.setPreferredSize(new Dimension(30, 30));
            b.setMaximumSize(b.getPreferredSize());
            b.setMinimumSize(b.getPreferredSize());
            b.addActionListener(new TargetActionListener());
        }
    }

    private class TargetActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JToggleButton actionButton = (JToggleButton)e.getSource();
            RelativeCoordinate actionCoordinate = targets.get(actionButton);
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
