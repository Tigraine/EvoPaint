/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
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

    public JRuleEditor(IRule rule) {
        this.rule = rule;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        GridBagLayout layout = new GridBagLayout();
        layout.setConstraints(this, constraints);
        //setLayout(layout);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new TitledBorder("Edit Rule"));

        add(Box.createHorizontalStrut(20));
        JLabel labelIf = new JLabel("<html><b>&nbsp;IF&nbsp;</b><html>", JLabel.CENTER);
        add(labelIf);

        JConditionList jConditionList = new JConditionList(rule.getConditions());
        add(jConditionList);


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
        
        add(new JAction(rule.getAction()));
        add(Box.createHorizontalStrut(20));
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

}
