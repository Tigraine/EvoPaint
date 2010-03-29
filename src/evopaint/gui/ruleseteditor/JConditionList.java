/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.conditions.EmptyCondition;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author tam
 */
public class JConditionList extends JPanel {
    //private List<ICondition> conditions;
    private JPanel panelForConditionWrappers;

    private void collapseAll() {
        Component [] components = panelForConditionWrappers.getComponents();
        for (int i = 0; i < components.length; i++) {
            ((JCondition)((JPanel)components[i]).getComponent(0)).collapse();
        }
    }

    public JConditionList(List<ICondition> conditions) {
        //this.conditions = conditions;
        //conditionWrappers = new ArrayList<JPanel>(conditions.size());

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
        buttonAnd.addActionListener(new AndButtonListener(this));
        constraints.gridy = 1;
        add(buttonAnd, constraints);

        // lay out conditions inside the wrapper panel
        constraints.gridy = 0;
        //constraints.gridwidth = GridBagConstraints.REMAINDER;
        //constraints.fill = GridBagConstraints.HORIZONTAL;
        for (ICondition condition : conditions) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            constraints.gridy = 0;
            panelForConditionWrappers.add(wrapper, constraints);
            constraints.gridy = panelForConditionWrappers.getComponentCount();;
            wrapper.add(new JCondition(condition,
                    new JConditionExpandedListener(),
                    new JConditionReplaceListener(),
                    new JConditionDeleteListener()),
                    constraints);
            //revalidate();
        }

        

    }

    private class AndButtonListener implements ActionListener {
        private JConditionList jConditionList;

        public AndButtonListener(JConditionList jConditionList) {
            this.jConditionList = jConditionList;
        }

        public void actionPerformed(ActionEvent e) {
            ICondition condition = new EmptyCondition(); // XXX maybe choose another? or place a placeholder?
            JCondition jCondition = new JCondition(condition,
                    new JConditionExpandedListener(),
                    new JConditionReplaceListener(),
                    new JConditionDeleteListener());

            JPanel wrapper = new JPanel(new GridBagLayout());
            
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 0;
            //constraints.gridwidth = GridBagConstraints.REMAINDER;
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

    private class JConditionReplaceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                ICondition prototype = (ICondition)((JComboBox)e.getSource()).getSelectedItem();
                ICondition condition = prototype.getClass().newInstance();
                JCondition newJCondition = new JCondition(condition,
                        new JConditionExpandedListener(),
                        new JConditionReplaceListener(),
                        new JConditionDeleteListener());
                Component [] components = panelForConditionWrappers.getComponents();
                for (int i = 0; i < components.length; i++) {
                    JPanel wrapper = (JPanel)components[i];
                    if (wrapper.getComponent(0) == ((JCondition)((JComboBox)e.getSource()).getParent().getParent())) {
                        wrapper.remove(0);
                        wrapper.add(newJCondition);
                        newJCondition.expand();
                        revalidate();
                        repaint();
                    }
                }
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

    private class JConditionDeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 0;
            //constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.gridy = 0;
            Component [] components = panelForConditionWrappers.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel wrapper = (JPanel)components[i];
                if (wrapper.getComponent(0) == ((JCondition)((JButton)e.getSource()).getParent().getParent())) {
                    wrapper.remove(0);
                    panelForConditionWrappers.remove(wrapper);
                    revalidate();
                    repaint();
                }
            }
            components = panelForConditionWrappers.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel wrapper = (JPanel)components[i];
                ((GridBagLayout)panelForConditionWrappers.getLayout()).setConstraints(wrapper, constraints);
                constraints.gridy++;
            }
        }
    }
}
