/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.gui.rulesetmanager;

import evopaint.Configuration;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.actions.ChangeEnergyAction;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.QualifiedMetaTarget;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JRuleEditorPanel extends JPanel {
    private Configuration configuration;
    private JConditionList jConditionList;
    private JActionButton jActionButton;
    private JActionTargetButton jActionTargetButton;
    private JQualifierList jQualifierList;

    public JRuleEditorPanel(Configuration configuration, Rule rule, ActionListener OKListener, ActionListener CancelListener) {
        this.configuration = configuration;
        
        setLayout(new BorderLayout(20, 20));
        setBorder(new LineBorder(getBackground(), 6));

        if (rule == null) {
            rule = new Rule(new ArrayList(){{
                add(new TrueCondition());
            }},
            new ChangeEnergyAction(0));
        }

        // rule panel
        JPanel rulePanel = new JPanel();
        rulePanel.setBackground(Color.WHITE);
        rulePanel.setLayout(new BoxLayout(rulePanel, BoxLayout.Y_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.insets = new Insets(10, 10, 10, 10);

        // if condition
        JPanel ifPanel = new JPanel();
        ifPanel.setLayout(new GridBagLayout());
        ifPanel.setBorder(new LineBorder(Color.GRAY));
        ifPanel.setBackground(new Color(0xF2F2F5));
        rulePanel.add(ifPanel);

        JLabel ifLabel = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>if</span><html>");
        ifPanel.add(ifLabel, c);

        jConditionList = new JConditionList(configuration, rule.getConditions());
        c.gridy = 1;
        ifPanel.add(jConditionList, c);

        // then action
        JPanel thenPanel = new JPanel();
        thenPanel.setLayout(new GridBagLayout());
        thenPanel.setBorder(new LineBorder(Color.GRAY));
        thenPanel.setBackground(new Color(0xF2F2F5));
        rulePanel.add(Box.createVerticalStrut(20));
        rulePanel.add(thenPanel);

        JLabel thenLabel = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>then</span><html>");
        c.gridy = 0;
        thenPanel.add(thenLabel, c);

        jActionButton = new JActionButton(configuration, rule.getAction());
        c.gridy = 1;
        thenPanel.add(jActionButton, c);


        // action target and qualifiers
        JPanel jActionTargetEditorPanel = new JPanel();
        jActionTargetEditorPanel.setBackground(new Color(0xF2F2F5));
        if (rule.getAction().getTarget() instanceof ActionMetaTarget) {
            jQualifierList = new JQualifierList(
                    ((ActionMetaTarget)rule.getAction().getTarget()).getQualifiers());
        } else {
            jQualifierList = new JQualifierList();
        }
        jActionTargetButton = new JActionTargetButton(rule.getAction().getTarget(), jQualifierList);

        jActionTargetEditorPanel.add(jActionTargetButton, c);

        c.fill = GridBagConstraints.REMAINDER;
        jActionTargetEditorPanel.add(jQualifierList, c);

        c.gridy = 2;
        thenPanel.add(jActionTargetEditorPanel, c);

        JPanel alignmentPanel = new JPanel();
        alignmentPanel.setLayout(new GridBagLayout());
        alignmentPanel.setBackground(Color.WHITE);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);
        alignmentPanel.add(rulePanel, constraints);

        JScrollPane scrollPaneForRulePanel = new JScrollPane(alignmentPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneForRulePanel.setBorder(new LineBorder(Color.GRAY));
        scrollPaneForRulePanel.setViewportBorder(null);
        scrollPaneForRulePanel.getVerticalScrollBar().setUnitIncrement(10);

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

    public Rule createRule() {
        List<Condition> conditions = jConditionList.createConditions();

        Action action = jActionButton.createAction();
        ITarget actionTarget = jActionTargetButton.createTarget();
        if (actionTarget instanceof QualifiedMetaTarget) {
            ((QualifiedMetaTarget)actionTarget).setQualifiers(jQualifierList.createQualifiers());
        }
        action.setTarget(actionTarget);

        return new Rule(conditions, action);
    }
    
}
