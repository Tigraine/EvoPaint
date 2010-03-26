/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
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
    private JConditionList jConditionList;
    private boolean expanded;

    public ICondition getCondition() {
        return condition;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public JCondition(final ICondition condition, final JConditionList jConditionList) {
        this.condition = condition;
        this.jConditionList = jConditionList;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // set up collapsed view
        collapsedPanel = new JPanel();
        collapsedPanel.setLayout(new GridBagLayout());
        JButton expandButton = new JButton(condition.toString());
        expandButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jConditionList.collapseAll();
                expand();
            }
        });
        collapsedPanel.add(expandButton);
        JButton deleteButtonCollapsed = new JButton("X");
        deleteButtonCollapsed.addActionListener(new DeleteButtonListener(this));
        collapsedPanel.add(deleteButtonCollapsed);

        // set up expanded view
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BorderLayout());

        JPanel panelInner = new JPanel();
        panelInner.setLayout(new BorderLayout());
        expandedPanel.add(panelInner, BorderLayout.CENTER);
        panelInner.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        panelInner.add(new JTargetPicker(), BorderLayout.WEST);

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
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }

        ComboBoxRenderer renderer = new ComboBoxRenderer();
        JComboBox comboBoxConditions = new JComboBox(model);
        comboBoxConditions.setRenderer(renderer);
        comboBoxConditions.addActionListener(new ComboBoxConditionsListener());
        panelInner.add(comboBoxConditions, BorderLayout.NORTH);

        JPanel panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setPreferredSize(new Dimension(100, 30));
        panelInner.add(panelParameters, BorderLayout.EAST);

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(2, 1, 0, 0));
        JButton buttonCollapse = new JButton("<");
        buttonCollapse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                collapse();
            }
        });
        panelButtons.add(buttonCollapse);
        JButton buttonDelete = new JButton("X");
        buttonDelete.addActionListener(new DeleteButtonListener(this));
        panelButtons.add(buttonDelete);
        expandedPanel.add(panelButtons, BorderLayout.EAST);

        collapsedPanel.setPreferredSize(new Dimension(
                (int)Math.max(panelInner.getPreferredSize().getWidth(), collapsedPanel.getPreferredSize().getWidth()),
                (int)collapsedPanel.getPreferredSize().getHeight()));
        
        collapse();
    }

    public void collapse() {
        expanded = false;
        removeAll();
        add(collapsedPanel);
        revalidate();
    }

    public void expand() {
        expanded = true;
        removeAll();
        add(expandedPanel);
        revalidate();
        SwingUtilities.getWindowAncestor(this).pack();
        // ^ this is way better than v
        //((JFrame)getParent().getParent().getParent().getParent().getParent().getParent()).pack();
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

    private class DeleteButtonListener implements ActionListener {
        private JCondition jCondition;

        public DeleteButtonListener(JCondition jCondition) {
            this.jCondition = jCondition;
        }
        
        public void actionPerformed(ActionEvent e) {
            jConditionList.remove(this.jCondition);
            jConditionList.revalidate();
        }
    }

    private class ComboBoxConditionsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((JButton)collapsedPanel.getComponent(0)).setText(((ICondition)((JComboBox)e.getSource()).getSelectedItem()).toString());
            //collapsedPanel.revalidate();
            //revalidate();
            //jConditionList.setPreferredSize(new Dimension(500, jConditionList.getPreferredSize().height));
            //jConditionList.revalidate();
        }

    }

}
