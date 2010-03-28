/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JTargetPicker extends JPanel implements ActionListener {
    private IdentityHashMap<JToggleButton,RelativeCoordinate> targets;

    public List<RelativeCoordinate> getTargetList() {
        List<RelativeCoordinate> ret = new ArrayList<RelativeCoordinate>(1);
        for (JToggleButton btn : targets.keySet()) {
            if (btn.isSelected()) {
                System.out.println("selected " + targets.get(btn));
            }
        }
        return ret;
    }

    public JTargetPicker() {
        setLayout(new GridLayout(3, 3));
        setBorder(new TitledBorder("targets"));

        targets = new IdentityHashMap<JToggleButton, RelativeCoordinate>();

        JToggleButton btn = new JToggleButton("\\");
        add(btn);
        targets.put(btn, RelativeCoordinate.NORTH_WEST);

        btn = new JToggleButton("|");
        add(btn);
        targets.put(btn, RelativeCoordinate.NORTH);

        btn = new JToggleButton("/");
        add(btn);
        targets.put(btn, RelativeCoordinate.NORTH_EAST);

        btn = new JToggleButton("-");
        add(btn);
        targets.put(btn, RelativeCoordinate.WEST);

        btn = new JToggleButton("*");
        add(btn);
        targets.put(btn, RelativeCoordinate.SELF);

        btn = new JToggleButton("-");
        add(btn);
        targets.put(btn, RelativeCoordinate.EAST);

        btn = new JToggleButton("/");
        add(btn);
        targets.put(btn, RelativeCoordinate.SOUTH_WEST);

        btn = new JToggleButton("|");
        add(btn);
        targets.put(btn, RelativeCoordinate.SOUTH);

        btn = new JToggleButton("\\");
        add(btn);
        targets.put(btn, RelativeCoordinate.SOUTH_EAST);

        // TODO evaluate if needed
        for (JToggleButton b : targets.keySet()) {
            b.setPreferredSize(new Dimension(25, 25));
            b.addActionListener(this);
        }

        setMaximumSize(new Dimension(90, 110));
    }

    public void actionPerformed(ActionEvent e) {
        for (JToggleButton btn : targets.keySet()) {
            if (e.getSource() == btn) {
                // nop
            }
        }
    }
}
