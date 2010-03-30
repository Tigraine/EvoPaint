/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.conditions.NoCondition;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author tam
 */
public class JConditionList extends JPanel {
    private List<ICondition> conditions;
    private List<JCondition> jConditions;
    private JPanel panelForConditionWrappers;

    public List<ICondition> getConditions() {
       // List<ICondition> conditions = new ArrayList<ICondition>();
//        for (JCondition jCondition : jConditions) {
  //          conditions.add(jCondition.getCondition());
    //    }
        return conditions;
    }

    public void setConditions(List<ICondition> conditions) {
        this.conditions = conditions;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 0;

        panelForConditionWrappers.removeAll();

        for (ICondition condition : conditions) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            constraints.gridy = 0;
            panelForConditionWrappers.add(wrapper, constraints);
            JCondition jCondition = new JCondition(new JConditionExpandedListener(),
                    new JConditionDeleteListener());
            jCondition.setCondition(condition);
            wrapper.add(jCondition, constraints);
            constraints.gridy++;
            //revalidate();
        }
    }

    private void collapseAll() {
        Component [] components = panelForConditionWrappers.getComponents();
        for (int i = 0; i < components.length; i++) {
            ((JCondition)((JPanel)components[i]).getComponent(0)).collapse();
        }
    }

    public JConditionList() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // lay out a wrapper panel and the "AND" button
        panelForConditionWrappers = new JPanel(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 0;
        add(panelForConditionWrappers, constraints);

        JButton buttonAnd = new JButton("AND");
        buttonAnd.addActionListener(new AndButtonListener());
        constraints.gridy = 1;
        add(buttonAnd, constraints);
    }

    private class AndButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ICondition condition = new NoCondition();
            conditions.add(condition);
            
            JCondition jCondition = new JCondition(new JConditionExpandedListener(),
                    new JConditionDeleteListener());
            jCondition.setCondition(condition);

            JPanel wrapper = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 0;
            wrapper.add(jCondition, constraints);

            constraints.gridy = panelForConditionWrappers.getComponentCount();
            panelForConditionWrappers.add(wrapper, constraints);
            panelForConditionWrappers.revalidate();
        }
    }

    private class JConditionExpandedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JToggleButton btn = (JToggleButton)e.getSource();
            JCondition jCondition = (JCondition)btn.getParent().getParent();
            if (btn.isSelected()) {
                collapseAll();
                jCondition.expand();
            } else {
                jCondition.collapse();
            }
        }
    }
    
    private class JConditionDeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JCondition jCondition = (JCondition)((JButton)e.getSource()).getParent().getParent();
            ICondition condition = (ICondition)jCondition.getCondition();
            System.out.println("from jCondition: " + condition.hashCode());
            for (int i = 0; i < conditions.size(); i++) {
                System.out.println("in conditions: " + conditions.get(i).hashCode());
            }

            // remove jCondition from wrapperList and most importantly.......

            Component [] components = panelForConditionWrappers.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel wrapper = (JPanel)components[i];
                if (wrapper.getComponent(0) == jCondition) {
                    wrapper.remove(0);
                    panelForConditionWrappers.remove(wrapper);
                    panelForConditionWrappers.revalidate();
                    panelForConditionWrappers.repaint();
                    System.out.println("removed " + jCondition.hashCode());
                    break;
                }
            }
            // .... update the layout, because the gridy constraint will NOT update automatically
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 0;
            components = panelForConditionWrappers.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel wrapper = (JPanel)components[i];
                ((GridBagLayout)panelForConditionWrappers.getLayout()).setConstraints(wrapper, constraints);
                constraints.gridy++;
            }
        }
    }
}
