/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.interfaces.IAction;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JAction extends JPanel implements ActionListener {
    private JButton closeButton;

    public JAction(IAction action) {

        setLayout(new BorderLayout(0, 0));
        setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        /* //automatic class discovery. sometimes slow and can be buggy depending on packaging.
        Set<Class> types = null;
        try {
            HashSet<String> packages = new HashSet<String>();
            packages.add("evopaint.pixel.rulebased.actions");
            Map<String, Set<Class>> m = ClassList.findClasses(
                    (new Configuration()).getClass().getClassLoader(), null, packages, null);
            types = m.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }*/

        for (IAction a : Configuration.availableActions) {
            model.addElement(a);
        }

        JComboBox comboBoxActions = new JComboBox(model);
        comboBoxActions.setRenderer(new NamedObjectListCellRenderer());
        comboBoxActions.setSelectedItem(action);
        comboBoxActions.setBorder(new LineBorder(getBackground(), 10));
        add(comboBoxActions, BorderLayout.NORTH);

        JTargetPicker targetPicker = new JTargetPicker();
        add(targetPicker, BorderLayout.EAST);

        JPanel panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setPreferredSize(new Dimension(100, 30));
        add(panelParameters, BorderLayout.WEST);

        setMaximumSize(getPreferredSize());
        //this.closeButton = new JButton("X");
        //this.closeButton.addActionListener(this);
        //add(this.closeButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.closeButton &&
                ((JPanel)getParent()).getComponentCount() > 1) {
            JPanel parent = (JPanel)getParent();
            parent.remove(this);
            parent.revalidate();
            return;
        }
    }
/*
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
            return (JLabel) defaultRenderer.getListCellRendererComponent(list, ((IAction)value).getName(), index,
        isSelected, cellHasFocus);
        }
    }
    */
}
