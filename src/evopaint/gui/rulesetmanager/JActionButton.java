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
import evopaint.pixel.rulebased.Action;
import evopaint.util.ExceptionHandler;
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
import javax.swing.border.BevelBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JActionButton extends JButton {

    private Configuration configuration;
    private Action createdAction;
    private JDialog dialog;
    private JComboBox comboBoxActions;
    private JPanel container;
    private JPanel parametersPanel;

    public JActionButton(Configuration configuration, Action actionArg) {
        this.configuration = configuration;
        this.createdAction = actionArg;

        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Edit Action", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setUndecorated(true);

        this.container = new JPanel();
        container.setBorder(new BevelBorder(BevelBorder.RAISED));
        container.setLayout(new GridBagLayout());
        dialog.add(container);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;

        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        for (Action a : Configuration.AVAILABLE_ACTIONS) {
            comboBoxModel.addElement(a);
        }

        comboBoxActions = new JComboBox(comboBoxModel);
        comboBoxActions.setRenderer(new NamedObjectListCellRenderer());
        Action selection = null;
        for (Action a : Configuration.AVAILABLE_ACTIONS) {
            if (a.getClass() == createdAction.getClass()) {
                selection = a;
            }
        }
        assert(selection != null);
        comboBoxActions.setSelectedItem(selection);
        comboBoxActions.addActionListener(new ComboBoxActionsListener());
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 5, 10);
        container.add(comboBoxActions, c);

        parametersPanel = new JParametersPanel(createdAction);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 10);
        container.add(parametersPanel, c);
        
        JPanel controlPanel = new JPanel();
        final JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                setText("<html>" + createdAction.toHTML() + "</html>");
            }
         });
        controlPanel.add(okButton);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 0, 10, 0);
        container.add(controlPanel, c);

        final JButton tis = this;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setLocationRelativeTo(tis);
                dialog.setVisible(true);
            }
        });

        setText("<html>" + createdAction.toHTML() + "</html>");
    }

    public JActionButton() {
    }

    public Action createAction() {
        if (createdAction != null) {
            return createdAction;
        }
        try {
            createdAction = ((Action)comboBoxActions.getSelectedItem()).getClass().newInstance();
        } catch (InstantiationException ex) {
            ExceptionHandler.handle(ex, true);
        } catch (IllegalAccessException ex) {
            ExceptionHandler.handle(ex, true);
        }
        return createdAction;
    }

    private class ComboBoxActionsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            createdAction = null;
            createdAction = createAction();
            container.remove(parametersPanel);
            parametersPanel = new JParametersPanel(createdAction);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(5, 5, 5, 10);
            container.add(parametersPanel, c);
            dialog.pack();
        }
    }

}
