/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.actions.RewardAction;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.ColorLikenessCondition;
import evopaint.pixel.PixelColor;
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

/**
 *
 * @author tam
 */
public class JRuleSetEditor extends JPanel {
    private List<ICondition> conditions;
    private List<IAction> actions;


    private JRuleList jRuleList;

    //private JPanel panelForConditions;

    public JRuleSetEditor(RuleSet ruleSet) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        jRuleList = new JRuleList(ruleSet);

       // JScrollPane scroll = new JScrollPane(rules, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
       //         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       // add(scroll);
        
        // add rule list to this panel
        JScrollPane scrollPaneForRuleList = new JScrollPane(jRuleList);
        scrollPaneForRuleList.setBorder(new TitledBorder("Rules"));
        add(scrollPaneForRuleList);

        add(new JRuleEditor(ruleSet.getRules().get(0)));
    }

    
}


