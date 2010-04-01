/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

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
import javax.swing.BoxLayout;
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
    JConditionList jConditionList;
    JAction jAction;

    public IRule getRule() {
        List<ICondition> conditions = jConditionList.getConditions();
        IAction action = jAction.getAction();
        return new Rule(conditions, action);
    }

    public void setRule(IRule rule) {
        jConditionList.setConditions(rule.getConditions());
        jAction.setAction(rule.getAction());
    }

    public JRuleEditor(ActionListener OKListener, ActionListener CancelListener) {
        setLayout(new BorderLayout(20, 20));
        setBorder(new LineBorder(getBackground(), 6));
        
        JPanel rulePanel = new JPanel();
        JScrollPane scrollPaneForRulePanel = new JScrollPane(rulePanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRulePanel.setBorder(new LineBorder(Color.GRAY));
        scrollPaneForRulePanel.setViewportBorder(null);
        add(scrollPaneForRulePanel, BorderLayout.CENTER);

        rulePanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel labelIf = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>&nbsp;IF&nbsp;</span><html>", JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        rulePanel.add(labelIf, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        jConditionList = new JConditionList();
        rulePanel.add(jConditionList, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        rulePanel.add(new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>&nbsp;THEN&nbsp;</span><html>", JLabel.CENTER), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.ipadx = 8;
        constraints.ipady = 8;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        jAction = new JAction();
        rulePanel.add(jAction, constraints);

        JPanel controlPanel = new JPanel();
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(OKListener);
        controlPanel.add(btnOK);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(CancelListener);
        controlPanel.add(btnCancel);

        //constraints.gridx = 0;
        //constraints.gridy = 2;
        //constraints.gridwidth = 2;
        add(controlPanel, BorderLayout.SOUTH);
    }
}
