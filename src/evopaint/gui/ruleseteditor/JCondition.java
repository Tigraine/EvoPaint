/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.util.ClassList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JCondition extends JPanel {
    private ICondition condition;
    private JPanel expandedPanel;
    private JPanel collapsedPanel;
    private ActionListener expandConditionListener;
    private boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public JCondition(ICondition condition, ActionListener expandConditionListener) {
        this.condition = condition;
        this.expandConditionListener = expandConditionListener;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // set up collapsed view
        collapsedPanel = new JPanel();
        collapsedPanel.setLayout(new GridLayout(1, 1, 0, 0));
        JButton expandButton = new JButton(condition.toString());
        expandButton.addActionListener(expandConditionListener);
        collapsedPanel.add(expandButton);


        // set up expanded view
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BorderLayout());
        expandedPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        expandedPanel.add(new JTargetPicker(), BorderLayout.WEST);

        // do the combo box magic
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        /* //automatic class discovery. sometimes slow and can be buggy depending on packaging.
        Set<Class> types = null;
        try {
            HashSet<String> packages = new HashSet<String>();
            packages.add("evopaint.pixel.rulebased.conditions");
            Map<String, Set<Class>> m = ClassList.findClasses(
                    (new Configuration()).getClass().getClassLoader(), null, packages, null);
            types = m.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        */

        for (Class type : Configuration.availableConditions) {
            try {
                model.addElement((ICondition) type.newInstance());
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
                //Logger.getLogger(JCondition.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
                //Logger.getLogger(JCondition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ComboBoxRenderer renderer = new ComboBoxRenderer();
        JComboBox comboBoxConditions = new JComboBox(model);
        comboBoxConditions.setRenderer(renderer);
        expandedPanel.add(comboBoxConditions, BorderLayout.NORTH);

        JPanel panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setPreferredSize(new Dimension(100, 30));
        expandedPanel.add(panelParameters, BorderLayout.EAST);

        collapsedPanel.setPreferredSize(new Dimension((int)expandedPanel.getPreferredSize().getWidth(), (int)collapsedPanel.getPreferredSize().getHeight()));
        collapse();
    }

    public void collapse() {
        expanded = false;
        removeAll();
        add(collapsedPanel);
    }

    public void expand() {
        expanded = true;
        removeAll();
        add(expandedPanel);
    }

    private class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        public ComboBoxRenderer() {
        }

        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {

            DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
            return (JLabel) defaultRenderer.getListCellRendererComponent(list, ((ICondition)value).getName(), index,
        isSelected, cellHasFocus);
        }
    }
}
