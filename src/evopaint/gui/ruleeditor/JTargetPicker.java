/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.util.mapping.RelativeCoordinate;
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
        targets.put(new JToggleButton("\\"), RelativeCoordinate.NORTH_WEST);
        targets.put(new JToggleButton("|"), RelativeCoordinate.NORTH);
        targets.put(new JToggleButton("/"), RelativeCoordinate.NORTH_EAST);
        targets.put(new JToggleButton("-"), RelativeCoordinate.WEST);
        targets.put(new JToggleButton("*"), RelativeCoordinate.SELF);
        targets.put(new JToggleButton("-"), RelativeCoordinate.EAST);
        targets.put(new JToggleButton("/"), RelativeCoordinate.SOUTH_WEST);
        targets.put(new JToggleButton("|"), RelativeCoordinate.SOUTH);
        targets.put(new JToggleButton("\\"), RelativeCoordinate.SOUTH_EAST);

        // TODO evaluate if needed
        for (JToggleButton btn : targets.keySet()) {
            btn.addActionListener(this);
        }
    }

    public void actionPerformed(ActionEvent e) {
        for (JToggleButton btn : targets.keySet()) {
            if (e.getSource() == btn) {
                // nop
            }
        }
    }
}
