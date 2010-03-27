/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
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


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // set up collapsed view
        collapsedPanel = new JPanel();
        collapsedPanel.setLayout(new BoxLayout(collapsedPanel, BoxLayout.X_AXIS));
        add(collapsedPanel);
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
        collapsedPanel.add(Box.createHorizontalGlue());

        // set up expanded view
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BoxLayout(expandedPanel, BoxLayout.X_AXIS));
        add(expandedPanel);
        JPanel expandedPanelContent = new JPanel();
        expandedPanelContent.setLayout(new BoxLayout(expandedPanelContent, BoxLayout.Y_AXIS));
        expandedPanelContent.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        expandedPanel.add(expandedPanelContent);
        expandedPanel.add(Box.createHorizontalGlue());

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

        for (Class type : Configuration.availableConditions) {
            try {
                model.addElement((ICondition) type.newInstance());
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }

        ComboBoxRenderer renderer = new ComboBoxRenderer();
        JComboBox comboBoxConditions = new JComboBox(model);
        comboBoxConditions.setRenderer(renderer);
        comboBoxConditions.addActionListener(new ComboBoxConditionsListener());
        comboBoxConditions.setBorder(new LineBorder(getBackground(), 10));
        expandedPanelContent.add(comboBoxConditions);



        Box wrapTargetsParameters = Box.createHorizontalBox();
        wrapTargetsParameters.add(new JTargetPicker());
        JPanel panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setPreferredSize(new Dimension(100, 30));
        wrapTargetsParameters.add(panelParameters);
        expandedPanelContent.add(wrapTargetsParameters);
        expandedPanelContent.setMaximumSize(expandedPanel.getPreferredSize());

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

    private class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        public ComboBoxRenderer() {
        }

        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {

            DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
            return (JLabel) defaultRenderer.getListCellRendererComponent(list, ((ICondition)value).getName(), index,
        isSelected, cellHasFocus);
        }
    }

    private class DeleteButtonListener implements ActionListener {
        private JCondition jCondition;

        public DeleteButtonListener(JCondition jCondition) {
            this.jCondition = jCondition;
        }
        
        public void actionPerformed(ActionEvent e) {
            jConditionList.remove(this.jCondition);
            jConditionList.revalidate();
        }
    }

    private class ComboBoxConditionsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((JToggleButton)collapsedPanel.getComponent(0)).setText(((ICondition)((JComboBox)e.getSource()).getSelectedItem()).toString());
            //collapsedPanel.revalidate();
            //revalidate();
            //expandedPanel.revalidate();
            //jConditionList.setPreferredSize(new Dimension(500, jConditionList.getPreferredSize().height));
            //jConditionList.revalidate();
            //((JPanel)jConditionList.getParent()).revalidate();
        }

    }

}
