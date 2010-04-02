/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.Configuration;
import evopaint.pixel.rulebased.conditions.NoCondition;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JCondition extends JButton {

    private Configuration configuration;
    private ICondition iCondition;
    private JDialog dialog;
    JComboBox comboBoxConditions;
    ComboBoxConditionsListener comboBoxConditionsListener;
    JTargetPicker targetPicker;
    JPanel panelParameters;

    public ICondition getICondition() {
        return iCondition;
    }

    public void setCondition(ICondition iCondition, boolean updateText) {
        this.iCondition = iCondition;
        if (updateText) {
            setText("<html>" + iCondition.toHTML() + "</html>");
        }

        ICondition selection = null;
        for (ICondition c : Configuration.availableConditions) {
            if (c.getClass() == iCondition.getClass()) {
                selection = c;
            }
        }
        assert(selection != null);
        comboBoxConditions.removeActionListener(comboBoxConditionsListener);
        comboBoxConditions.setSelectedItem(selection);
        comboBoxConditions.addActionListener(comboBoxConditionsListener);

        if (iCondition instanceof NoCondition) {
            comboBoxConditions.setPreferredSize(new Dimension(200, 25));
            targetPicker.setVisible(false);
            panelParameters.setVisible(false);
            return;
        }
        
        targetPicker.setDirections(iCondition.getDirections());

        panelParameters.removeAll();
        LinkedHashMap<String,JComponent> parameters = iCondition.getParametersForGUI(configuration);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        for (String string : parameters.keySet()) {
            constraints.gridx = 0;
            constraints.insets = new Insets(0, 0, 5, 10);
            panelParameters.add(new JLabel(string + ":"), constraints);
            constraints.gridx = 1;
            constraints.insets = new Insets(0, 0, 5, 0);
            panelParameters.add(parameters.get(string), constraints);
            constraints.gridy = constraints.gridy + 1;
        }

        targetPicker.setVisible(true);
        panelParameters.setVisible(true);
    }

    public JCondition(Configuration configuration) {
        this.configuration = configuration;
        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Edit Action", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
        dialog.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {
                setText("<html>" + iCondition.toHTML() + "</html>");
            }
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        // do the combo box magic
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        for (ICondition c : Configuration.availableConditions) {
            comboBoxModel.addElement(c);
        }
        comboBoxConditions = new JComboBox(comboBoxModel);
        comboBoxConditions.setRenderer(new NamedObjectListCellRenderer());
        // we do not set the action listener here, because it will fire on
        // setSelectedIndex()
        comboBoxConditionsListener = new ComboBoxConditionsListener();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 5, 10);
        dialog.add(comboBoxConditions, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 10, 5, 5);
        targetPicker = new JTargetPicker();
        dialog.add(targetPicker, constraints);

        panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setLayout(new GridBagLayout());
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 5, 5, 10);
        dialog.add(panelParameters, constraints);

        JPanel controlPanel = new JPanel();
        //controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        final JButton btnOK = new JButton("OK");
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                setText(iCondition.toString());
            }
         });
        controlPanel.add(btnOK);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(5, 0, 10, 0);
        dialog.add(controlPanel, constraints);

        final JButton tis = this;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setLocationRelativeTo(tis);
                dialog.setVisible(true);
            }
        });
    }

    private class ComboBoxConditionsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                ICondition prototype = (ICondition)((JComboBox)e.getSource()).getSelectedItem();
                ICondition newCondition = prototype.getClass().newInstance();
                setCondition(newCondition, false);
                dialog.pack();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
}
