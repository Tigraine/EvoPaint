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

import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.Configuration;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JConditionButton extends JButton {

    private Configuration configuration;
    private JDialog dialog;
    private JComboBox comboBoxConditions;
    private JPanel parametersPanel;
    private Condition condition;
    
    public JConditionButton(Configuration configuration, final JConditionList jConditionList, Condition passedCondition) {
        this.configuration = configuration;
        this.condition = passedCondition;
        
        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Edit Condition", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
        dialog.setUndecorated(true);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        // do the combo box magic
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        for (Condition c : Configuration.AVAILABLE_CONDITIONS) {
            comboBoxModel.addElement(c);
        }
        comboBoxConditions = new JComboBox(comboBoxModel);
        comboBoxConditions.setRenderer(new NamedObjectListCellRenderer());
        Condition selection = null;
        for (Condition c : Configuration.AVAILABLE_CONDITIONS) {
            if (c.getClass() == condition.getClass()) {
                selection = c;
            }
        }
        assert(selection != null);
        comboBoxConditions.setSelectedItem(selection);
        comboBoxConditions.addActionListener(new ComboBoxConditionsListener());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(comboBoxConditions, constraints);
        if (condition instanceof TrueCondition) {
            parametersPanel = new JPanel();
        } else {
            parametersPanel = new JParametersPanel(condition);
        }
        constraints.gridx = 0;
        constraints.gridy = 1;
        dialog.add(parametersPanel, constraints);

        JPanel controlPanel = new JPanel();
        final JButton okButton = new JButton("OK");
        final JConditionButton tis = this;
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                updateText();
                jConditionList.updateConditionTargetButton(tis, condition);
            }
         });
        controlPanel.add(okButton);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(5, 0, 10, 0);
        dialog.add(controlPanel, constraints);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setLocationRelativeTo(tis);
                dialog.setVisible(true);
            }
        });

        updateText();
    }

    public Condition createTargetLessCondition() {
        if (condition != null) {
            return condition;
        }
        try {
            condition = ((Condition)comboBoxConditions.getSelectedItem()).getClass().newInstance();
        } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return condition;
    }

    public void updateText() {
        setText("<html>" + condition.toHTML() + "</html>");
    }

    private class ComboBoxConditionsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            condition = null;
            condition = createTargetLessCondition();
            dialog.remove(parametersPanel);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(5, 5, 5, 5);
            if (condition instanceof TrueCondition) {
                parametersPanel = new JPanel();
            } else {
                parametersPanel = new JParametersPanel(condition);
            }
            dialog.add(parametersPanel, c);
            dialog.pack();
        }
    }
    
}
