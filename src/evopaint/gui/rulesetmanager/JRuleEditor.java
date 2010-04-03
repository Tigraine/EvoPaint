/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

import evopaint.Configuration;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/**
 *
 * @author tam
 */
public class JRuleEditor extends JPanel {
    private Configuration configuration;
    JConditionList jConditionList;
    JAction jAction;

    public IRule getRule() {
        List<ICondition> conditions = jConditionList.getConditions();
        IAction action = jAction.getIAction();
        return new Rule(conditions, action);
    }

    public void setRule(IRule rule) {
        jConditionList.setConditions(rule.getConditions());
        jAction.setIAction(rule.getAction(), true);
    }

    public JRuleEditor(Configuration configuration, ActionListener OKListener, ActionListener CancelListener) {
        this.configuration = configuration;
        setLayout(new BorderLayout(20, 20));
        setBorder(new LineBorder(getBackground(), 6));

        // rule panel
        JPanel rulePanel = new JPanel();
        rulePanel.setBackground(Color.WHITE);

        JLabel labelIf = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>IF</span><html>");
        rulePanel.add(labelIf);

        jConditionList = new JConditionList(configuration);
        rulePanel.add(jConditionList);

        JLabel thenLabel = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>THEN</span><html>");
        rulePanel.add(thenLabel);

        jAction = new JAction(configuration);
        rulePanel.add(jAction);

        JScrollPane scrollPaneForRulePanel = new JScrollPane(rulePanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRulePanel.setBorder(new LineBorder(Color.GRAY));
        scrollPaneForRulePanel.setViewportBorder(null);

        add(scrollPaneForRulePanel, BorderLayout.CENTER);
        
        // control panel
        JPanel controlPanel = new JPanel();
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(OKListener);
        controlPanel.add(btnOK);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(CancelListener);
        controlPanel.add(btnCancel);

        add(controlPanel, BorderLayout.SOUTH);
    }

}
