/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.rulebased.AbstractAction;
import evopaint.pixel.rulebased.actions.IdleAction;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ITargetSelection;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author tam
 */
public class JAction extends JButton {

    private Configuration configuration;
    private IAction iAction;
    private JDialog dialog;
    private JComboBox comboBoxActions;
    private ComboBoxActionsListener comboBoxActionsListener;
    private JRadioButton oneRadio;
    private JRadioButton allRadio;
    private JComboBox targetSelectionComboBox;
    private TargetSelectionComboBoxListener targetSelectionComboBoxListener;
    private JTargetPicker jTargetPicker;
    private JPanel targetSelectionPanel;
    private JPanel panelParameters;

    public IAction getIAction() { // cos getAction is taken GRRR
        return iAction;
    }

    public void setIAction(final IAction iAction, boolean updateText) {
        this.iAction = iAction;
        if (updateText) {
            setText("<html>" + iAction.toHTML() + "</html>");
        }

        IAction selection = null;
        for (IAction a : Configuration.AVAILABLE_ACTIONS) {
            if (a.getClass() == iAction.getClass()) {
                selection = a;
            }
        }
        assert(selection != null);
        comboBoxActions.removeActionListener(comboBoxActionsListener);
        comboBoxActions.setSelectedItem(selection);
        comboBoxActions.addActionListener(comboBoxActionsListener);

        if (iAction instanceof IdleAction) {
            comboBoxActions.setPreferredSize(new Dimension(200, 25)); // or else it will get crippled
            targetSelectionPanel.setVisible(false);
            panelParameters.setVisible(false);
            return;
        }

        panelParameters.removeAll();
        LinkedHashMap<String,JComponent> parametersMap = new LinkedHashMap<String, JComponent>();
        parametersMap = iAction.parametersCallbackGUI(parametersMap);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(iAction.getCost(), 0, Integer.MAX_VALUE, 1);
        JSpinner costSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        costSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iAction.setCost((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        parametersMap.put("Cost", costSpinner);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        for (String string : parametersMap.keySet()) {
            constraints.gridx = 0;
            constraints.insets = new Insets(0, 0, 5, 10);
            panelParameters.add(new JLabel(string + ":"), constraints);
            constraints.gridx = 1;
            constraints.insets = new Insets(0, 0, 5, 0);
            panelParameters.add(parametersMap.get(string), constraints);
            constraints.gridy = constraints.gridy + 1;
        }

        if (iAction.getMode() == AbstractAction.ONE) {
            oneRadio.setSelected(true);
        } else {
            allRadio.setSelected(true);
        }

        jTargetPicker.setDirections(iAction.getTargetSelection().getDirections());

        ITargetSelection selectedTargetSelection = null;
        for (ITargetSelection ts : Configuration.AVAILABLE_TARGET_SELECTIONS) {
            if (ts.getClass() == iAction.getTargetSelection().getClass()) {
                selectedTargetSelection = ts;
            }
        }
        assert(selectedTargetSelection != null);
        targetSelectionComboBox.removeActionListener(targetSelectionComboBoxListener);
        targetSelectionComboBox.setSelectedItem(selection);
        targetSelectionComboBox.addActionListener(targetSelectionComboBoxListener);

        targetSelectionPanel.setVisible(true);
        panelParameters.setVisible(true);
    }

    private void setTargetSelection(ITargetSelection targetSelection) {
        iAction.setTargetSelection(targetSelection);
    }

    public JAction(Configuration configuration) {
        this.configuration = configuration;
        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Edit Action", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // this is creepy. THIS event in THIS listener will fire whenever
                // you close a dialog... any dialog...
                if (iAction != null) { // and this is a just as creepy hackaround
                    setText("<html>" + iAction.toHTML() + "</html>");
                }
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        for (IAction a : Configuration.AVAILABLE_ACTIONS) {
            comboBoxModel.addElement(a);
        }

        comboBoxActions = new JComboBox(comboBoxModel);
        comboBoxActions.setRenderer(new NamedObjectListCellRenderer());
        // we do not set the action listener here, because it will fire on
        // setSelectedIndex()
        comboBoxActionsListener = new ComboBoxActionsListener();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 5, 10);
        dialog.add(comboBoxActions, constraints);


        // BEGIN target selection panel

        targetSelectionPanel = new JPanel();
        targetSelectionPanel.setBorder(new TitledBorder("Target Selection"));

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        oneRadio = new JRadioButton("One of");
        oneRadio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                iAction.setMode(AbstractAction.ONE);
            }
        });
        oneRadio.setSelected(true);
        radioPanel.add(oneRadio);
        allRadio = new JRadioButton("All of");
        allRadio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                iAction.setMode(AbstractAction.ALL);
            }
        });
        radioPanel.add(allRadio);
        ButtonGroup group = new ButtonGroup();
        group.add(oneRadio);
        group.add(allRadio);

        targetSelectionPanel.add(radioPanel);

        jTargetPicker = new JTargetPicker();
        targetSelectionPanel.add(jTargetPicker);

        targetSelectionComboBox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (ITargetSelection ts : Configuration.AVAILABLE_TARGET_SELECTIONS) {
            model.addElement(ts);
        }
        targetSelectionComboBox.setModel(model);
        targetSelectionComboBox.setRenderer(new NamedObjectListCellRenderer());
        targetSelectionComboBoxListener = new TargetSelectionComboBoxListener(); // added in setAction
        targetSelectionComboBox.setPreferredSize(new Dimension(200, targetSelectionComboBox.getPreferredSize().height));
        targetSelectionPanel.add(targetSelectionComboBox);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(5, 10, 5, 5);
        dialog.add(targetSelectionPanel, constraints);

        // END target selection panel



        panelParameters = new JPanel();
        panelParameters.setBorder(new TitledBorder("Parameters"));
        panelParameters.setLayout(new GridBagLayout());
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 5, 5, 10);
        dialog.add(panelParameters, constraints);
        
        JPanel controlPanel = new JPanel();
        final JButton btnOK = new JButton("OK");
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                setText(iAction.toString());
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

    private class ComboBoxActionsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                IAction prototype = (IAction)((JComboBox)e.getSource()).getSelectedItem();
                IAction newAction = prototype.getClass().newInstance();
                setIAction(newAction, false);
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

    private class TargetSelectionComboBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                ITargetSelection prototype = (ITargetSelection)((JComboBox)e.getSource()).getSelectedItem();
                ITargetSelection newTargetSelection = prototype.getClass().newInstance();
                newTargetSelection.setDirections(iAction.getTargetSelection().getDirections());
                setTargetSelection(newTargetSelection);
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
