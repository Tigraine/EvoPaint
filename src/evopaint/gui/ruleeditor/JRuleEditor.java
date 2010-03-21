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
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author tam
 */
public class JRuleEditor extends JPanel {
    private JRuleList jRuleList;
    private JConditionPool jConditionPool;
    private JActionPool jActionPool;

    public JRuleEditor() {

        List<ICondition> conditions = new ArrayList();
        List<IAction> actions = new ArrayList();
        List<IRule> rules = new ArrayList();

        conditions.add(new EnergyCondition(RelativeCoordinate.HERE, 20, EnergyCondition.GREATER_OR_EQUAL));
        conditions.add(new LikeColorCondition("is a little blue", RelativeCoordinate.NORTH, new PixelColor(0xFF), 0.1, PixelColor.COMPARE_BY_BLUE));

        actions.add(new AssimilationAction(20, RelativeCoordinate.WEST, PixelColor.MIX_HSB));
        actions.add(new RewardAction(10));

        List conditionsA = new ArrayList();
        conditionsA.add(conditions.get(0));
        rules.add(new Rule(conditionsA, actions.get(0)));

        List conditionsB = new ArrayList();
        conditionsB.add(conditions.get(0));
        conditionsB.add(conditions.get(1));
        rules.add(new Rule(conditionsB, actions.get(1)));

        RuleSet ruleSet = new RuleSet("test ruleset", rules);

        jRuleList = new JRuleList(ruleSet);
        jConditionPool = new JConditionPool(conditions);
        jActionPool = new JActionPool(actions);


       // JScrollPane scroll = new JScrollPane(rules, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
       //         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       // add(scroll);
        
        // add rule list to this panel
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalStrut(50));
        b.add(jRuleList);
        b.add(Box.createHorizontalStrut(50));
        add(b);

        JPanel poolsPanel = new JPanel();
        poolsPanel.setLayout(new GridLayout());
        poolsPanel.add(jConditionPool);
        poolsPanel.add(jActionPool);
        add(poolsPanel);

        //setPreferredSize(new Dimension(500,100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}

