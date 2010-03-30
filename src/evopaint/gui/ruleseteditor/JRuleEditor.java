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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
    private boolean dirty;
    JConditionList jConditionList;
    JAction jAction;
    JPanel fuckingWrapper;

    public boolean isDirty() {
        return dirty;
    }

    public IRule getRule() {
        if (dirty) {
            List<ICondition> conditions = jConditionList.getConditions();
            IAction action = jAction.getAction();
            return new Rule(conditions, action);
        }
        return rule;
    }

    public void setRule(IRule rule) {
        rule = rule;
        dirty = false;
        jConditionList.setConditions(rule.getConditions());
        jAction.setAction(rule.getAction());
    }

    public JRuleEditor() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Edit Rule"));

        fuckingWrapper = new JPanel();
        fuckingWrapper.setLayout(new GridBagLayout());
        add(fuckingWrapper, BorderLayout.WEST);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel labelIf = new JLabel("<html><b>&nbsp;IF&nbsp;</b><html>", JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        fuckingWrapper.add(labelIf, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        jConditionList = new JConditionList();
        fuckingWrapper.add(jConditionList, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        fuckingWrapper.add(new JLabel("<html><b>&nbsp;THEN&nbsp;</b><html>", JLabel.CENTER), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.ipadx = 8;
        constraints.ipady = 8;
        jAction = new JAction();
        fuckingWrapper.add(jAction, constraints);

        JPanel controlPanel = new JPanel();
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dirty = true;
                setVisible(false);
                getParent().getComponent(0).setVisible(true);
            }
        });
        controlPanel.add(btnOK);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                getParent().getComponent(0).setVisible(true);
            }
        });
        
        controlPanel.add(btnCancel);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        fuckingWrapper.add(controlPanel, constraints);
    }
}
