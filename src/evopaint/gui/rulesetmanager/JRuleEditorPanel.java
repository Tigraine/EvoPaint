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
import evopaint.pixel.rulebased.actions.IdleAction;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedActionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedConditionTarget;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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
    JConditionList jConditionList;
    JActionButton jAction;
    JTargetButton jActionTarget;


    public JRuleEditorPanel(Configuration configuration, IRule rule, ActionListener OKListener, ActionListener CancelListener) {
        this.configuration = configuration;
        setLayout(new BorderLayout(20, 20));
        setBorder(new LineBorder(getBackground(), 6));

        if (rule == null) {
            rule = new Rule(new ArrayList(){{
                add(new TrueCondition(new SpecifiedConditionTarget(new ArrayList<RelativeCoordinate>())));
            }},
            new IdleAction(0, new SpecifiedActionTarget(new ArrayList<RelativeCoordinate>())));
        }

        // rule panel
        JPanel rulePanel = new JPanel();
        rulePanel.setBorder(new LineBorder(Color.GRAY));
        rulePanel.setBackground(new Color(0xF2F2F5));

        JLabel labelIf = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>IF</span><html>");
        rulePanel.add(labelIf);

        jConditionList = new JConditionList(configuration, rule.getConditions());
        rulePanel.add(jConditionList);

        JLabel thenLabel = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>THEN</span><html>");
        rulePanel.add(thenLabel);

        jAction = new JActionButton(configuration, this, rule.getAction());
        rulePanel.add(jAction);

        jActionTarget = new JTargetButton(configuration, rule.getAction().getTarget(), JTargetButton.ACTION);
        rulePanel.add(jActionTarget);

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

        updateActionTargetButton(rule.getAction());
    }

    public IRule createRule() {
        List<Condition> conditions = jConditionList.createConditions();
        Action action = jAction.createTargetLessAction();
        action.setTarget((IActionTarget)jActionTarget.createTarget());
        return new Rule(conditions, action);
    }

    public void updateActionTargetButton(Action action) {
        if (action instanceof IdleAction) {
            jActionTarget.setVisible(false);
        } else {
            jActionTarget.setVisible(true);
        }
    }
    
}
