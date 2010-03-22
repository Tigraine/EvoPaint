/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.interfaces.IAction;
import evopaint.interfaces.ICondition;
import evopaint.interfaces.IRule;
import evopaint.pixel.Rule;
import evopaint.pixel.RuleSet;
import evopaint.pixel.actions.AssimilationAction;
import evopaint.pixel.actions.RewardAction;
import evopaint.pixel.conditions.EnergyCondition;
import evopaint.pixel.conditions.LikeColorCondition;
import evopaint.pixel.PixelColor;
import evopaint.pixel.actions.ActionWrapper;
import evopaint.pixel.conditions.ConditionWrapper;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

/**
 *
 * @author tam
 */
public class JRuleEditor extends JPanel {
    private List<ICondition> conditions;
    private List<IAction> actions;


    private JRuleList jRuleList;

    //private JPanel panelForConditions;

    public JRuleEditor() {

        List<ConditionWrapper> wrappedConditions = new ArrayList();
        List<ActionWrapper> wrappedActions = new ArrayList();
        List<IRule> rules = new ArrayList();

        wrappedConditions.add(new ConditionWrapper(RelativeCoordinate.SELF,
                new EnergyCondition(20, EnergyCondition.GREATER_OR_EQUAL)));
        wrappedConditions.add(new ConditionWrapper(RelativeCoordinate.SELF,
                new LikeColorCondition("is a little blue", new PixelColor(0xFF), 0.1, PixelColor.COMPARE_BY_BLUE)));

        wrappedActions.add(new ActionWrapper(RelativeCoordinate.WEST,
                new AssimilationAction(-20, PixelColor.MIX_HSB)));
        wrappedActions.add(new ActionWrapper(RelativeCoordinate.SELF,
                new RewardAction(10)));

        List<ActionWrapper> actionsA = new ArrayList<ActionWrapper>();
        actionsA.add(wrappedActions.get(0));

        List<ActionWrapper> actionsB = new ArrayList<ActionWrapper>();
        actionsB.add(wrappedActions.get(1));
        actionsB.add(wrappedActions.get(0));

        List conditionsA = new ArrayList();
        conditionsA.add(wrappedConditions.get(0));
        rules.add(new Rule(conditionsA, actionsA));

        List conditionsB = new ArrayList();
        conditionsB.add(wrappedConditions.get(0));
        conditionsB.add(wrappedConditions.get(1));
        rules.add(new Rule(conditionsB, actionsB));

        RuleSet ruleSet = new RuleSet("test ruleset", rules);

        jRuleList = new JRuleList(ruleSet);


        this.conditions = new ArrayList(wrappedConditions.size());
        for (ConditionWrapper wrappedCondition : wrappedConditions) {
            conditions.add(wrappedCondition.getCondition());
        }
        actions = new ArrayList(wrappedActions.size());
        for (ActionWrapper wrappedAction : wrappedActions) {
            actions.add(wrappedAction.getAction());
        }

       // JScrollPane scroll = new JScrollPane(rules, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
       //         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       // add(scroll);
        
        // add rule list to this panel
        JScrollPane scrollPaneForRuleList = new JScrollPane(jRuleList);
        scrollPaneForRuleList.setBorder(new TitledBorder("Rules"));
        add(scrollPaneForRuleList);

        JPanel panelEdit = new JPanel();
        panelEdit.setLayout(new GridBagLayout());
        add(panelEdit);

        JLabel labelIf = new JLabel("<html><b>&nbsp;IF&nbsp;</b><html>", JLabel.CENTER);
        panelEdit.add(labelIf);

        // conditions
        JPanel panelForConditionsWrapper = new JPanel();
        panelForConditionsWrapper.setLayout(new BoxLayout(panelForConditionsWrapper, BoxLayout.Y_AXIS));
        panelEdit.add(panelForConditionsWrapper);

        JPanel panelForConditions = new JPanel();
        panelForConditions.setLayout(new BoxLayout(panelForConditions, BoxLayout.Y_AXIS));
        panelForConditionsWrapper.add(panelForConditions);

        JButton buttonANDCondition = new JButton("AND");
        buttonANDCondition.addActionListener(new AddConditionButtonListener(panelForConditions));
        panelForConditionsWrapper.add(buttonANDCondition);
        
        JCondition jCondition = new JCondition(conditions);
        panelForConditions.add(jCondition);
        

        panelEdit.add(new JLabel("<html><b>&nbsp;THEN&nbsp;</b><html>", JLabel.CENTER));

        
        // actions
        JPanel panelForActionsWrapper = new JPanel();
        panelForActionsWrapper.setLayout(new BoxLayout(panelForActionsWrapper, BoxLayout.Y_AXIS));
        panelEdit.add(panelForActionsWrapper);

        JPanel panelForActions = new JPanel();
        panelForActions.setLayout(new BoxLayout(panelForActions, BoxLayout.Y_AXIS));
        panelForActionsWrapper.add(panelForActions);

        JButton buttonANDAction = new JButton("AND");
        buttonANDAction.addActionListener(new AddActionButtonListener(panelForActions));
        panelForActionsWrapper.add(buttonANDAction);
        
        JAction jAction = new JAction(actions);
        panelForActions.add(jAction);
        


        //setPreferredSize(new Dimension(500,100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private class AddActionButtonListener implements ActionListener {
        private JPanel panelForActions;

        public AddActionButtonListener(JPanel panelForActions) {
            this.panelForActions = panelForActions;
        }
        
        public void actionPerformed(ActionEvent e) {
            this.panelForActions.add(new JAction(actions));
        }
    }

    private class AddConditionButtonListener implements ActionListener {
        private JPanel panelForConditions;

        public AddConditionButtonListener(JPanel panelForConditions) {
            this.panelForConditions = panelForConditions;
        }
        
        public void actionPerformed(ActionEvent e) {
            this.panelForConditions.add(new JCondition(conditions));
        }
    }
}


