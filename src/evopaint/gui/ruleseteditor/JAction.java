/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.gui.ruleseteditor.util.NamedObjectListCellRenderer;
import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.rulebased.actions.NoAction;
import evopaint.pixel.rulebased.interfaces.IAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author tam
 */
public class JAction extends JPanel {
    
    private IAction action;
    //private JButton closeButton;
    JComboBox comboBoxActions;
    ComboBoxActionsListener comboBoxActionsListener;
    JTargetPicker targetPicker;
    JPanel panelParameters;

    public IAction getAction() {
        return action;
    }

    public void setAction(final IAction action) {
        this.action = action;
        IAction selection = null;
        for (IAction a : Configuration.availableActions) {
            if (a.getClass() == action.getClass()) {
                selection = a;
            }
        }
        assert(selection != null);
        comboBoxActions.removeActionListener(comboBoxActionsListener);
        comboBoxActions.setSelectedItem(selection);
        comboBoxActions.addActionListener(comboBoxActionsListener);


        if (action instanceof NoAction) {
            comboBoxActions.setPreferredSize(new Dimension(200, 25));
            targetPicker.setVisible(false);
            panelParameters.setVisible(false);
            return;
        }

        panelParameters.removeAll();
        LinkedHashMap<String,JComponent> parameters = action.getParametersForGUI();

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(action.getCost(), 0, Integer.MAX_VALUE, 1);
        JSpinner costSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        costSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                action.setCost((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        parameters.put("Cost per target", costSpinner);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
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

        targetPicker.setDirections(action.getDirections());
        targetPicker.setVisible(true);
        panelParameters.setVisible(true);
        revalidate();
        repaint();
    }

    public JAction() {

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (IAction a : Configuration.availableActions) {
            model.addElement(a);
        }

        comboBoxActions = new JComboBox(model);
        comboBoxActions.setRenderer(new NamedObjectListCellRenderer());
        // we do not set the action listener here, because it will fire on
        // setSelectedIndex()
        comboBoxActionsListener = new ComboBoxActionsListener();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(comboBoxActions, constraints);

        panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(panelParameters, constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 1;
        targetPicker = new JTargetPicker();
        add(targetPicker, constraints);
    }
/*
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.closeButton &&
                ((JPanel)getParent()).getComponentCount() > 1) {
            JPanel parent = (JPanel)getParent();
            parent.remove(this);
            parent.revalidate();
            return;
        }
    }
*/
    private class ComboBoxActionsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                IAction prototype = (IAction)((JComboBox)e.getSource()).getSelectedItem();
                IAction newAction = prototype.getClass().newInstance();
                setAction(newAction);
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
                //Logger.getLogger(JCondition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
