/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author tam
 */
public class JAction extends JPanel implements ActionListener {
    private JButton closeButton;

    public JAction(IAction action, DefaultComboBoxModel availableAcctions) {
        
        JComboBox comboBoxActions = new JComboBox(availableAcctions);
        add(comboBoxActions);

        JTargetPicker targetPicker = new JTargetPicker();
        add(targetPicker);

        this.closeButton = new JButton("X");
        this.closeButton.addActionListener(this);
        add(this.closeButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.closeButton &&
                ((JPanel)getParent()).getComponentCount() > 1) {
            JPanel parent = (JPanel)getParent();
            parent.remove(this);
            parent.revalidate();
            return;
        }
    }


}
