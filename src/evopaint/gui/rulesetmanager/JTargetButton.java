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

import evopaint.pixel.rulebased.Action;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.targeting.ITargeted;
import evopaint.pixel.rulebased.targeting.ITarget;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JTargetButton extends JButton {

    private JDialog dialog;
    private JPanel targetPanel;

    public JTargetButton(final ITargeted targeted) {

        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Choose Target", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
        dialog.setUndecorated(true);

        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.insets = new Insets(5, 5, 5, 5);


        if (targeted instanceof Action) {
            targetPanel = new JActionTargetPanel(targeted.getTarget());
        }
        else if (targeted instanceof Condition) {
            targetPanel = new JConditionTargetPanel(targeted.getTarget());
        }
        else {
            assert(false);
        }

        c.gridy = 1;
        dialog.add(targetPanel, c);

        JPanel controlPanel = new JPanel();
        final JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                targeted.setTarget(createTarget());
                setText("<html>" + targeted.getTarget().toHTML() + "</html>");
            }
         });
        controlPanel.add(okButton);
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
         });
        controlPanel.add(cancelButton);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        dialog.add(controlPanel, c);
        final JButton tis = this;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setLocationRelativeTo(tis);
                dialog.setVisible(true);
            }
        });

        setText("<html>" + targeted.getTarget().toHTML() + "</html>");
    }

    public JTargetButton() {
    }

    private ITarget createTarget() {
        ITarget target = null;
        if (targetPanel instanceof JActionTargetPanel) {
            target = ((JActionTargetPanel)targetPanel).createActionTarget();
        }
        else if (targetPanel instanceof JConditionTargetPanel) {
            target = ((JConditionTargetPanel)targetPanel).createConditionTarget();
        }
        else {
            assert (false);
        }
        return target;
    }

}
