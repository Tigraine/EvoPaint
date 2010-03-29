/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.gui.ruleseteditor.util.NamedObjectListCellRenderer;
import evopaint.Configuration;
import evopaint.pixel.rulebased.interfaces.ICondition;
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
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JCondition extends JPanel {
    private ICondition condition;
    private JPanel expandedPanel;
    private JPanel collapsedPanel;
    //private JConditionList jConditionList;
    JPanel panelParameters;

    public ICondition getCondition() {
        return condition;
    }

    public JCondition(final ICondition condition,
            ActionListener expansionListener,
            ActionListener replaceListener,
            ActionListener deleteListener) {
        this.condition = condition;
        //this.jConditionList = jConditionList;

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        // set up collapsed view
        collapsedPanel = new JPanel();
        collapsedPanel.setLayout(getLayout());
        add(collapsedPanel, constraints);
        JToggleButton expandButton = new JToggleButton(condition.toString());
        expandButton.addActionListener(expansionListener);
        collapsedPanel.add(expandButton);
        JButton deleteButtonCollapsed = new JButton("X");
        deleteButtonCollapsed.addActionListener(deleteListener);
        collapsedPanel.add(deleteButtonCollapsed);

        expandedPanel = new JPanel();
        //expandedPanel.setLayout(new BoxLayout(expandedPanel, BoxLayout.Y_AXIS));
        expandedPanel.setLayout(new GridBagLayout());
        expandedPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipadx = 8;
        constraints.ipady = 8;
        add(expandedPanel, constraints);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        // do the combo box magic
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        /* //automatic class discovery. sometimes slow and can be buggy depending on packaging.
        Set<Class> types = null;
        try {
            HashSet<String> packages = new HashSet<String>();
            packages.add("evopaint.pixel.rulebased.conditions");
            Map<String, Set<Class>> m = ClassList.findClasses(
                    (new Configuration()).getClass().getClassLoader(), null, packages, null);
            types = m.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        */

        ICondition selection = null;
        for (ICondition c : Configuration.availableConditions) {
            model.addElement(c);
            if (c.getClass() == condition.getClass()) {
                selection = c;
            }
        }
        assert(selection != null);

        JComboBox comboBoxConditions = new JComboBox(model);
        comboBoxConditions.setRenderer(new NamedObjectListCellRenderer());
        //comboBoxConditions.setMinimumSize(new Dimension(25, 25));
        comboBoxConditions.setSelectedItem(selection);
        comboBoxConditions.addActionListener(replaceListener);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        expandedPanel.add(comboBoxConditions, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        expandedPanel.add(new JTargetPicker(condition.getDirections()), constraints);

        panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setLayout(new GridBagLayout());
        constraints.gridx = 1;
        constraints.gridy = 1;
        expandedPanel.add(panelParameters, constraints);

        LinkedHashMap<String,JComponent> parameters = condition.getParametersForGUI();

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

        collapse();
    }

    public void collapse() {
        JToggleButton ruleButton = (JToggleButton)collapsedPanel.getComponent(0);
        ruleButton.setText(condition.toString());
        if (ruleButton.isSelected()) {
            ruleButton.setSelected(false);
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
}
