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
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedConditionTarget;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JConditionList extends JPanel {
    private Configuration configuration;
    private List<JTargetButton> jConditionTargets;
    private List<JConditionButton> jConditions;
    private JPanel panelForConditionWrappers;

    public List<Condition> createConditions() {
        List<Condition> conditions = new ArrayList<Condition>();
        for (int i = 0; i < jConditions.size(); i++) {
            Condition c = jConditions.get(i).createTargetLessCondition();
            c.setTarget((IConditionTarget)jConditionTargets.get(i).createTarget());
            conditions.add(c);
        }
        return conditions;
    }

    public JConditionList(Configuration configuration, List<Condition> conditions) {
        this.configuration = configuration;
        jConditionTargets = new ArrayList<JTargetButton>();
        jConditions = new ArrayList<JConditionButton>();
  
        setLayout(new BorderLayout(10, 5));
        setBackground(new Color(0xF2F2F5));

        panelForConditionWrappers = new JPanel();
        panelForConditionWrappers.setLayout(new BoxLayout(panelForConditionWrappers, BoxLayout.Y_AXIS));
        add(panelForConditionWrappers, BorderLayout.CENTER);

        for (Condition condition : conditions) {
            addCondition(condition);
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(0xF2F2F5));
        JButton buttonAnd = new JButton("AND");
        buttonAnd.addActionListener(new AndButtonListener());
        controlPanel.add(buttonAnd);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public JConditionList() {
    }

    public void addCondition(Condition condition) {

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBackground(new Color(0xF2F2F5));

        JTargetButton jConditionTargetButton =
                new JTargetButton(configuration, condition.getTarget(), JTargetButton.CONDITION);
        wrapper.add(jConditionTargetButton);
        jConditionTargets.add(jConditionTargetButton);

        JConditionButton jConditionButton = new JConditionButton(configuration, this, condition);
        jConditions.add(jConditionButton);
        updateConditionTargetButton(jConditionButton, condition);

        wrapper.add(jConditionButton);

        JButton btnDelete = new JButton(new ImageIcon(getClass().getResource("icons/button-delete_condition.png")));
        btnDelete.setPreferredSize(new Dimension(btnDelete.getPreferredSize().height, btnDelete.getPreferredSize().height));
        btnDelete.addActionListener(new JConditionDeleteListener(jConditionButton, jConditionTargetButton));
        wrapper.add(btnDelete);

        panelForConditionWrappers.add(wrapper);
        panelForConditionWrappers.revalidate(); // will not display anything without this revalidate here
    }

    public void updateConditionTargetButton(JConditionButton jCondition, Condition condition) {
        if (condition instanceof TrueCondition) {
            int index = jConditions.indexOf(jCondition);
            jConditionTargets.get(index).setVisible(false);
        } else {
            int index = jConditions.indexOf(jCondition);
            jConditionTargets.get(index).setVisible(true);
        }
    }

    private class AndButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Condition condition = new TrueCondition(new SpecifiedConditionTarget(new ArrayList()));
            addCondition(condition);
        }
    }
    
    private class JConditionDeleteListener implements ActionListener {
        private JConditionButton jCondition;
        private JTargetButton jConditionTarget;

        public JConditionDeleteListener(JConditionButton jCondition, JTargetButton jConditionTarget) {
            this.jCondition = jCondition;
            this.jConditionTarget = jConditionTarget;
        }

        public void actionPerformed(ActionEvent e) {

            jConditions.remove(jCondition);
            jConditionTargets.remove(jConditionTarget);

            Component [] components = panelForConditionWrappers.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel wrapper = (JPanel)components[i];
                if (wrapper.getComponent(1) == jCondition) {
                    //wrapper.remove(0);
                    panelForConditionWrappers.remove(wrapper);
                    panelForConditionWrappers.revalidate();
                    panelForConditionWrappers.repaint();
                    break;
                }
            }

            if (jConditions.size() == 0) {
                addCondition(new TrueCondition(new SpecifiedConditionTarget(new ArrayList())));
            }
        }
    }
}
