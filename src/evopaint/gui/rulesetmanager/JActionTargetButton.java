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

import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.MetaTarget;
import evopaint.pixel.rulebased.targeting.SingleTarget;
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
import javax.swing.border.BevelBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JActionTargetButton extends JButton {

    private JActionTargetPanel targetPanel;
    private JDialog dialog;
    private ITarget createdTarget;

    public JActionTargetButton(final ITarget targetArg, final JQualifierList qualifierList) {
        this.createdTarget = targetArg;
        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Choose Target", true);
        dialog.setUndecorated(true);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        dialog.add(mainPanel);

        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.insets = new Insets(5, 5, 5, 5);

        targetPanel = new JActionTargetPanel(targetArg);

        c.gridy = 1;
        mainPanel.add((JPanel)targetPanel, c);

        JPanel controlPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                createdTarget = targetPanel.createTarget();
                setText("<html>" + createdTarget.toHTML() + "</html>");
                if (createdTarget instanceof MetaTarget &&
                        false == qualifierList.isEnabled()) {
                    qualifierList.setEnabled(true);
                } else if (createdTarget instanceof SingleTarget &&
                        true == qualifierList.isEnabled()) {
                    qualifierList.setEnabled(false);
                }
                dialog.dispose();
            }
         });
        controlPanel.add(okButton);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(controlPanel, c);

        final JActionTargetButton tis = this;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setLocationRelativeTo(tis);
                dialog.setVisible(true);
            }
        });

        setText("<html>" + targetArg.toHTML() + "</html>");
    }

    public JActionTargetButton() {
    }

    public ITarget createTarget() {
        assert (createdTarget != null);
        return createdTarget;
    }

}
