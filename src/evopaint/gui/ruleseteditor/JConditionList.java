/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.conditions.NoCondition;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author tam
 */
public class JConditionList extends JPanel {
    private Configuration configuration;
    private List<JCondition> jConditions;
    private JPanel panelForConditionWrappers;

    public List<ICondition> getConditions() {
        List<ICondition> conditions = new ArrayList<ICondition>();
        for (JCondition jCondition : jConditions) {
            conditions.add(jCondition.getICondition());
        }
        return conditions;
    }

    public void setConditions(List<ICondition> conditions) {
        jConditions.clear();
        panelForConditionWrappers.removeAll();

        for (ICondition condition : conditions) {
            addCondition(condition);
        }
    }

    public void addCondition(ICondition condition) {
        JCondition jCondition = new JCondition(configuration);
        jCondition.setCondition(condition, true);

        jConditions.add(jCondition);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.add(jCondition);

        JButton btnDelete = new JButton("X");
        btnDelete.addActionListener(new JConditionDeleteListener(jCondition));
        wrapper.add(btnDelete);

        panelForConditionWrappers.add(wrapper);
        panelForConditionWrappers.revalidate(); // will not display anything without this revalidate here
    }

    public JConditionList(Configuration configuration) {
        this.configuration = configuration;
        jConditions = new ArrayList<JCondition>();
        setLayout(new BorderLayout(10, 5));

        JLabel labelIf = new JLabel("<html><span style='color: #0000E6; font-weight: bold;'>IF</span><html>");
        add(labelIf, BorderLayout.WEST);

        panelForConditionWrappers = new JPanel();
        panelForConditionWrappers.setLayout(new BoxLayout(panelForConditionWrappers, BoxLayout.Y_AXIS));
        panelForConditionWrappers.setBorder(new LineBorder(Color.GRAY));
        add(panelForConditionWrappers, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton buttonAnd = new JButton("AND");
        buttonAnd.addActionListener(new AndButtonListener());
        controlPanel.add(buttonAnd);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private class AndButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ICondition condition = new NoCondition();
            addCondition(condition);
        }
    }
    
    private class JConditionDeleteListener implements ActionListener {
        private JCondition jCondition;

        public JConditionDeleteListener(JCondition jCondition) {
            this.jCondition = jCondition;
        }

        public void actionPerformed(ActionEvent e) {

            jConditions.remove(jCondition);

            Component [] components = panelForConditionWrappers.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel wrapper = (JPanel)components[i];
                if (wrapper.getComponent(0) == jCondition) {
                    wrapper.remove(0);
                    panelForConditionWrappers.remove(wrapper);
                    panelForConditionWrappers.revalidate();
                    panelForConditionWrappers.repaint();
                    break;
                }
            }

            if (jConditions.size() == 0) {
                addCondition(new NoCondition());
            }
        }
    }
}
