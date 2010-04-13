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
import evopaint.pixel.rulebased.targeting.IDirected;
import evopaint.pixel.rulebased.targeting.ITarget;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JTargetButton extends JButton {

    private JPanel targetPanel;
    private JDialog dialog;
    private Container contentPaneBackup;

    public JPanel getTargetPanel() {
        return targetPanel;
    }

    public JTargetButton(final IDirected targeted) {

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

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
        mainPanel.add(targetPanel, c);

        final JButton tis = this;
        JPanel controlPanel = new JPanel();
        final JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                targeted.setTarget(createTarget());
                setText("<html>" + targeted.getTarget().toHTML() + "</html>");
                dialog.setContentPane(contentPaneBackup);
                dialog.pack();
            }
         });
        controlPanel.add(okButton);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(controlPanel, c);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // just replace the content pane of the condition/action dialog
                dialog = ((JDialog)SwingUtilities.getWindowAncestor(tis));
                contentPaneBackup = dialog.getContentPane();
                dialog.setContentPane(mainPanel);
                dialog.pack();
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
