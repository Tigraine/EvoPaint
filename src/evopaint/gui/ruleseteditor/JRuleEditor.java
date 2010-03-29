/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
        
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Edit Rule"));

        JPanel fuckingWrapper = new JPanel();
        fuckingWrapper.setLayout(new GridBagLayout());
        add(fuckingWrapper, BorderLayout.WEST);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel labelIf = new JLabel("<html><b>&nbsp;IF&nbsp;</b><html>", JLabel.CENTER);
        constraints.gridx = 0;
        fuckingWrapper.add(labelIf, constraints);

        JConditionList jConditionList = new JConditionList(rule.getConditions());
        constraints.gridx = 1;
        fuckingWrapper.add(jConditionList, constraints);

        constraints.gridx = 2;
        fuckingWrapper.add(new JLabel("<html><b>&nbsp;THEN&nbsp;</b><html>", JLabel.CENTER), constraints);

        constraints.gridx = 3;
        constraints.ipadx = 8;
        constraints.ipady = 8;
        fuckingWrapper.add(new JAction(rule.getAction()), constraints);
    }
}
