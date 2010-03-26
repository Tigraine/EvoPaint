/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleEditor extends JPanel {
    private IRule rule;

    public IRule getRule() {
        return rule;
    }

    public void setRule(IRule rule) {
        this.rule = rule;
    }

    public JRuleEditor(IRule rule, DefaultComboBoxModel availableConditions, DefaultComboBoxModel availableActions) {
        this.rule = rule;

        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Edit Rule"));

        JLabel labelIf = new JLabel("<html><b>&nbsp;IF&nbsp;</b><html>", JLabel.CENTER);
        add(labelIf);

        // conditions
        JPanel panelConditionsWrapper = new JPanel();
        panelConditionsWrapper.setLayout(new BoxLayout(panelConditionsWrapper, BoxLayout.Y_AXIS));
        add(panelConditionsWrapper);

        JPanel panelForConditions = new JPanel();
        panelForConditions.setLayout(new BoxLayout(panelForConditions, BoxLayout.Y_AXIS));
        panelConditionsWrapper.add(panelForConditions);

        JButton buttonANDCondition = new JButton("AND");
        buttonANDCondition.addActionListener(new AddConditionButtonListener(panelForConditions));
        panelConditionsWrapper.add(buttonANDCondition);

        for (ICondition condition : rule.getConditions()) {
            JCondition jCondition = new JCondition(condition, availableConditions);
            panelForConditions.add(jCondition);
        }


        add(new JLabel("<html><b>&nbsp;THEN&nbsp;</b><html>", JLabel.CENTER));


        // actions
        /* // code for multiple actions
        JPanel panelForActionsWrapper = new JPanel();
        panelForActionsWrapper.setLayout(new BoxLayout(panelForActionsWrapper, BoxLayout.Y_AXIS));
        add(panelForActionsWrapper);

        JPanel panelForActions = new JPanel();
        panelForActions.setLayout(new BoxLayout(panelForActions, BoxLayout.Y_AXIS));
        panelForActionsWrapper.add(panelForActions);

        JButton buttonANDAction = new JButton("AND");
        buttonANDAction.addActionListener(new AddActionButtonListener(panelForActions));
        panelForActionsWrapper.add(buttonANDAction);

        JAction jAction = new JAction(actions);
        panelForActions.add(jAction);
        */
        
        add(new JAction(rule.getAction(), availableActions));
    }

    /* // for multiple actions
    private class AddActionButtonListener implements ActionListener {
        private JPanel panelForActions;

        public AddActionButtonListener(JPanel panelForActions) {
            this.panelForActions = panelForActions;
        }

        public void actionPerformed(ActionEvent e) {
            this.panelForActions.add(new JAction(actions));
        }
    }
    */
    
    private class AddConditionButtonListener implements ActionListener {
        private JPanel panelForConditions;

        public AddConditionButtonListener(JPanel panelForConditions) {
            this.panelForConditions = panelForConditions;
        }

        public void actionPerformed(ActionEvent e) {
            //this.panelForConditions.add(new JCondition(conditions));
        }
    }
}
