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

import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.util.ExceptionHandler;
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
public class JQualifierButton extends JButton {

    private JDialog dialog;
    private JQualifierComboBox qualifierComboBox;
    private JPanel parametersPanel;
    private Qualifier createdQualifier;
    private JPanel container;
    
    public JQualifierButton(final JQualifierList jQualifierList, Qualifier qualifierArg) {
        this.createdQualifier = (Qualifier)qualifierArg.getCopy();

        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Edit Qualifier", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setUndecorated(true);

        this.container = new JPanel();
        container.setBorder(new BevelBorder(BevelBorder.RAISED));
        container.setLayout(new GridBagLayout());
        dialog.add(container);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        qualifierComboBox = new JQualifierComboBox(createdQualifier);

        qualifierComboBox.addActionListener(new QualifierComboBoxListener());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        container.add(qualifierComboBox, constraints);
        
        parametersPanel = new JParametersPanel(createdQualifier);
        constraints.gridx = 0;
        constraints.gridy = 1;
        container.add(parametersPanel, constraints);

        JPanel controlPanel = new JPanel();
        final JButton okButton = new JButton("OK");
        final JQualifierButton tis = this;
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                setText("<html>" + createdQualifier.toHTML() + "</html>");
            }
         });
        controlPanel.add(okButton);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(5, 0, 10, 0);
        container.add(controlPanel, constraints);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setLocationRelativeTo(tis);
                dialog.setVisible(true);
            }
        });

        setText("<html>" + qualifierArg.toHTML() + "</html>");
    }

    public Qualifier createQualifier() {
        if (createdQualifier != null) {
            return createdQualifier;
        }
        try {
            createdQualifier = qualifierComboBox.getSelectedItem().getClass().newInstance();
        } catch (InstantiationException ex) {
            ExceptionHandler.handle(ex);
        } catch (IllegalAccessException ex) {
            ExceptionHandler.handle(ex);
        }

        return createdQualifier;
    }

    private class QualifierComboBoxListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            createdQualifier = null;
            createdQualifier = createQualifier();
            container.remove(parametersPanel);

            parametersPanel = new JParametersPanel(createdQualifier);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(5, 5, 5, 5);
            container.add(parametersPanel, c);
            
            dialog.pack();
        }
    }
    
}
