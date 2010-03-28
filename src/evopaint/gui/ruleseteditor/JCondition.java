/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
    private JConditionList jConditionList;
    JPanel panelParameters;
    private boolean expanded;

    public ICondition getCondition() {
        return condition;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public JCondition(final ICondition condition, final JConditionList jConditionList) {
        this.condition = condition;
        this.jConditionList = jConditionList;

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        // set up collapsed view
        collapsedPanel = new JPanel();
        collapsedPanel.setLayout(getLayout());
        add(collapsedPanel, constraints);
        JToggleButton expandButton = new JToggleButton(condition.toString());
        expandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (((JToggleButton)e.getSource()).isSelected()) {
                    jConditionList.collapseAll();
                    expand();
                } else {
                    collapse();
                }
            }
        });
        collapsedPanel.add(expandButton);
        JButton deleteButtonCollapsed = new JButton("X");
        deleteButtonCollapsed.addActionListener(new DeleteButtonListener(this));
        collapsedPanel.add(deleteButtonCollapsed);

        expandedPanel = new JPanel();
        //expandedPanel.setLayout(new BoxLayout(expandedPanel, BoxLayout.Y_AXIS));
        expandedPanel.setLayout(new GridBagLayout());
        expandedPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(expandedPanel, constraints);

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

        comboBoxConditions.setSelectedItem(selection);
        comboBoxConditions.addActionListener(new ComboBoxConditionsListener(this));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        expandedPanel.add(comboBoxConditions, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        expandedPanel.add(new JTargetPicker(), constraints);

        panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setLayout(new GridBagLayout());
        constraints.gridx = 1;
        constraints.gridy = 1;
        expandedPanel.add(panelParameters, constraints);

        LinkedHashMap<String,JComponent> parameters = condition.getParametersForGUI();
        
        constraints.gridy = 0;
        for (String string : parameters.keySet()) {
            constraints.gridx = 0;
            panelParameters.add(new JLabel(string), constraints);
            constraints.gridx = 1;
            panelParameters.add(parameters.get(string), constraints);
            constraints.gridy = constraints.gridy + 1;
        }

        collapse();
    }

    public void collapse() {
        expanded = false;
        if (((JToggleButton)collapsedPanel.getComponent(0)).isSelected()) {
            ((JToggleButton)collapsedPanel.getComponent(0)).setSelected(false);
        }
        expandedPanel.setVisible(false);
        //revalidate();
    }

    public void expand() {
        expanded = true;
        expandedPanel.setVisible(true);
        //collapsedPanel.revalidate();
        //expandedPanel.revalidate();
        //revalidate();
        //((JPanel)getParent()).revalidate();
        //SwingUtilities.getWindowAncestor(this).pack();
        // ^ this is way better than v
        //((JFrame)getParent().getParent().getParent().getParent().getParent().getParent()).pack();
    }

    private class DeleteButtonListener implements ActionListener {
        private JCondition jCondition;

        public DeleteButtonListener(JCondition jCondition) {
            this.jCondition = jCondition;
        }
        
        public void actionPerformed(ActionEvent e) {
            jConditionList.removeCondition(this.jCondition);
            jConditionList.revalidate();
        }
    }

    private class ComboBoxConditionsListener implements ActionListener {
        private JCondition jCondition;

        public ComboBoxConditionsListener(JCondition jCondition) {
            this.jCondition = jCondition;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                ICondition prototype = (ICondition)((JComboBox)e.getSource()).getSelectedItem();
                ICondition condition = prototype.getClass().newInstance();
                ((JToggleButton) collapsedPanel.getComponent(0)).setText(condition.toString());
                JCondition newJCondition = new JCondition(condition, jConditionList);
                ((JConditionList) getParent()).replaceCondition(jCondition, newJCondition);
                newJCondition.expand();
                jConditionList.revalidate();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
                //Logger.getLogger(JCondition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    
}
