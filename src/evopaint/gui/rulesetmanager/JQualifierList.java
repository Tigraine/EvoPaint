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
import evopaint.pixel.rulebased.conditions.ExistenceCondition;
import evopaint.pixel.rulebased.targeting.IQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.ExistenceQualifier;
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
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JQualifierList extends JPanel {
    private List<JQualifierComboBox> jQualifiers;
    private JPanel panelForQualifierWrappers;
    private JButton andButton;
    private JLabel whichLabel;

    public List<IQualifier> createQualifiers() {
        List<IQualifier> qualifiers  = new ArrayList<IQualifier>();
        for (int i = 0; i < jQualifiers.size(); i++) {
            IQualifier q = jQualifiers.get(i).getSelectedItem();
            qualifiers.add(q);
        }
        return qualifiers;
    }

    public JQualifierList(List<IQualifier> qualifiers) {
        
        jQualifiers = new ArrayList<JQualifierComboBox>();
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBackground(new Color(0xF2F2F5));

        whichLabel = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>which</span><html>");
        add(whichLabel);

        JPanel container = new JPanel();
        add(container);
        container.setLayout(new BorderLayout(10, 5));
        container.setBackground(new Color(0xF2F2F5));

        panelForQualifierWrappers = new JPanel();
        panelForQualifierWrappers.setLayout(new BoxLayout(panelForQualifierWrappers, BoxLayout.Y_AXIS));
        container.add(panelForQualifierWrappers, BorderLayout.CENTER);

        for (IQualifier q : qualifiers) {
            addQualifier(q);
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(0xF2F2F5));
        andButton = new JButton("<html><span style='color: #0000E6; font-weight: bold;'>and</span></html>");
        andButton.addActionListener(new AndButtonListener());
        controlPanel.add(andButton);
        container.add(controlPanel, BorderLayout.SOUTH);

        if (qualifiers.size() == 0) {
            addQualifier(ExistenceQualifier.getInstance());
            setEnabled(false);
        }

    }

    public JQualifierList() {
        this(new ArrayList<IQualifier>());
    }

    public void addQualifier(IQualifier qualifier) {

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBackground(new Color(0xF2F2F5));

        JQualifierComboBox jQualifierComboBox = new JQualifierComboBox(qualifier);
        jQualifiers.add(jQualifierComboBox);

        wrapper.add(jQualifierComboBox);

        JButton btnDelete = new JButton(new ImageIcon(getClass().getResource("icons/button-delete_condition.png")));
        btnDelete.setPreferredSize(new Dimension(btnDelete.getPreferredSize().height, btnDelete.getPreferredSize().height));
        btnDelete.addActionListener(new JQualifierDeleteListener(jQualifierComboBox));
        wrapper.add(btnDelete);

        panelForQualifierWrappers.add(wrapper);
        panelForQualifierWrappers.revalidate(); // will not display anything without this revalidate here
    }

    @Override
    public void setEnabled(boolean bool) {
        super.setEnabled(bool);
        Component [] wrappers = panelForQualifierWrappers.getComponents();
        for (Component wrapper : wrappers) {
            ((JPanel)wrapper).getComponent(0).setEnabled(bool);
            ((JPanel)wrapper).getComponent(1).setEnabled(bool);
        }
        andButton.setEnabled(bool);
        if (bool == true) {
            whichLabel.setText("<html><span style='color: #0000E6; font-weight: bold;'>which</span><html>");
            andButton.setText("<html><span style='color: #0000E6; font-weight: bold;'>and</span><html>");
        } else {
            whichLabel.setText("<html><span style='color: #A0A0A0; font-weight: bold;'>which</span><html>");
            andButton.setText("<html><span style='color: #A0A0A0; font-weight: bold;'>and</span><html>");
        }
    }

    private class AndButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            IQualifier qualifier = Configuration.AVAILABLE_QUALIFIERS.get(0);
            addQualifier(qualifier);
        }
    }
    
    private class JQualifierDeleteListener implements ActionListener {
        private JQualifierComboBox jQualifier;

        public JQualifierDeleteListener(JQualifierComboBox jQualifier) {
            this.jQualifier = jQualifier;
        }

        public void actionPerformed(ActionEvent e) {

            jQualifiers.remove(jQualifier);

            Component [] components = panelForQualifierWrappers.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel wrapper = (JPanel)components[i];
                if (wrapper.getComponent(0) == jQualifier) {
                    panelForQualifierWrappers.remove(wrapper);
                    panelForQualifierWrappers.revalidate();
                    panelForQualifierWrappers.repaint();
                    break;
                }
            }

            if (jQualifiers.size() == 0) {
                addQualifier(Configuration.AVAILABLE_QUALIFIERS.get(0));
            }
        }
    }
}
