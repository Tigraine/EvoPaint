/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.gui.ruleseteditor.util.NamedObjectListCellRenderer;
import evopaint.Configuration;
import evopaint.pixel.rulebased.conditions.NoCondition;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JCondition extends JPanel {
    private ICondition condition;

    private JPanel collapsedPanel;
    JToggleButton expandButton;

    private JPanel expandedPanel;
    JComboBox comboBoxConditions;
    ComboBoxConditionsListener comboBoxConditionsListener;
    JTargetPicker targetPicker;
    JPanel panelParameters;

    public ICondition getCondition() {
        return condition;
    }

    public void setCondition(ICondition condition) {
        this.condition = condition;

        expandButton.setText(condition.toString());

        ICondition selection = null;
        for (ICondition c : Configuration.availableConditions) {
            if (c.getClass() == condition.getClass()) {
                selection = c;
            }
        }
        assert(selection != null);
        comboBoxConditions.removeActionListener(comboBoxConditionsListener);
        comboBoxConditions.setSelectedItem(selection);
        comboBoxConditions.addActionListener(comboBoxConditionsListener);

        if (condition instanceof NoCondition) {
            comboBoxConditions.setPreferredSize(new Dimension(200, 25));
            targetPicker.setVisible(false);
            panelParameters.setVisible(false);
            return;
        }
        targetPicker.setDirections(condition.getDirections());


        panelParameters.removeAll();
        LinkedHashMap<String,JComponent> parameters = condition.getParametersForGUI();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        for (String string : parameters.keySet()) {
            constraints.gridx = 0;
            constraints.insets = new Insets(0, 0, 5, 10);
            panelParameters.add(new JLabel(string + ":"), constraints);
            constraints.gridx = 1;
            constraints.insets = new Insets(0, 0, 5, 0);
            panelParameters.add(parameters.get(string), constraints);
            constraints.gridy = constraints.gridy + 1;
        }

        targetPicker.setVisible(true);
        panelParameters.setVisible(true);
        revalidate();
        repaint();
    }

    public JCondition(ActionListener expansionListener,
            ActionListener deleteListener) {
        //this.condition = condition;
        //this.jConditionList = jConditionList;

        setLayout(new GridBagLayout());
        //setBackground(Color.WHITE);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        // set up collapsed view
        collapsedPanel = new JPanel();
        collapsedPanel.setLayout(getLayout());
        add(collapsedPanel, constraints);
        expandButton = new JToggleButton();
        expandButton.addActionListener(expansionListener);
        collapsedPanel.add(expandButton);
        JButton deleteButtonCollapsed = new JButton("X");
        deleteButtonCollapsed.addActionListener(deleteListener);
        collapsedPanel.add(deleteButtonCollapsed);

        expandedPanel = new JPanel();
        //expandedPanel.setLayout(new BoxLayout(expandedPanel, BoxLayout.Y_AXIS));
        expandedPanel.setLayout(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipadx = 8;
        constraints.ipady = 8;
        add(expandedPanel, constraints);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        // do the combo box magic
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (ICondition c : Configuration.availableConditions) {
            model.addElement(c);
        }
        comboBoxConditions = new JComboBox(model);
        comboBoxConditions.setRenderer(new NamedObjectListCellRenderer());
        comboBoxConditionsListener = new ComboBoxConditionsListener();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        expandedPanel.add(comboBoxConditions, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        targetPicker = new JTargetPicker();
        expandedPanel.add(targetPicker, constraints);

        panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setLayout(new GridBagLayout());
        constraints.gridx = 1;
        constraints.gridy = 1;
        expandedPanel.add(panelParameters, constraints);

        collapse();
    }

    public void collapse() {
        if (condition != null) {
            expandButton.setText(condition.toString());
            if (expandButton.isSelected()) {
                expandButton.setSelected(false);
            }
        }
        //remove(expandedPanel);
        expandedPanel.setVisible(false);
        revalidate();
    }

    public void expand() {
        if (((JToggleButton)collapsedPanel.getComponent(0)).isSelected() == false) {
            ((JToggleButton)collapsedPanel.getComponent(0)).setSelected(true);
        }
        expandedPanel.setVisible(true);
        revalidate();
    }

    private class ComboBoxConditionsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                ICondition prototype = (ICondition)((JComboBox)e.getSource()).getSelectedItem();
                ICondition condition = prototype.getClass().newInstance();
                setCondition(condition);
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
}
