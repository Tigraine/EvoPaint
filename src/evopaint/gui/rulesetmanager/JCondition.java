/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.JRangeSlider;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedHashMap;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author tam
 */
public class JCondition extends JButton {

    private Configuration configuration;
    private ICondition condition;
    private JRangeSlider jRangeSlider;
    private JLabel lowValueLabel;
    private JLabel highValueLabel;
    private JPanel quantifiedTargetsPanel;
    private JDialog dialog;
    private JComboBox comboBoxConditions;
    private ComboBoxConditionsListener comboBoxConditionsListener;
    private JTargetPicker targetPicker;
    private JPanel panelParameters;

    public ICondition getICondition() {
        condition.setMin((Integer)jRangeSlider.getLowValue());
        condition.setMax((Integer)jRangeSlider.getHighValue());
        return condition;
    }

    public void setCondition(ICondition iCondition, boolean updateText) {
        this.condition = iCondition;
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

        jRangeSlider.setMaximum(condition.getDirections().size());
        jRangeSlider.setLowValue(condition.getMin());
        jRangeSlider.setHighValue(condition.getMax());
        lowValueLabel.setText(Integer.toString(condition.getMin()));
        highValueLabel.setText(Integer.toString(condition.getMax()));

        if (iCondition instanceof TrueCondition) {
            comboBoxConditions.setPreferredSize(new Dimension(200, 25));
            quantifiedTargetsPanel.setVisible(false);
            panelParameters.setVisible(false);
            return;
        }
        
        targetPicker.setDirections(iCondition.getDirections());

        panelParameters.removeAll();
        LinkedHashMap<String,JComponent> parameters =
                iCondition.parametersCallbackGUI(new LinkedHashMap<String,JComponent>());

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

        quantifiedTargetsPanel.setVisible(true);
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
                setText("<html>" + condition.toHTML() + "</html>");
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


        quantifiedTargetsPanel = new JPanel();
        quantifiedTargetsPanel.setBorder(new TitledBorder("Quantified Targets"));
        quantifiedTargetsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel rangePanel = new JPanel();
        rangePanel.setLayout(new BorderLayout());

        JPanel lowAlignmentPanel = new JPanel();
        lowAlignmentPanel.setLayout(new BorderLayout());
        JPanel lowPanel = new JPanel();
        JLabel lowTextLabel = new JLabel("Min:");
        lowPanel.add(lowTextLabel);
        lowValueLabel = new JLabel("0");
        lowPanel.add(lowValueLabel);
        lowAlignmentPanel.add(lowPanel, BorderLayout.SOUTH);
        rangePanel.add(lowAlignmentPanel, BorderLayout.WEST);

        jRangeSlider = new JRangeSlider(0, 0, 0, 0, JRangeSlider.VERTICAL, JRangeSlider.RIGHTLEFT_BOTTOMTOP);
        jRangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                lowValueLabel.setText(Integer.toString(jRangeSlider.getLowValue()));
                highValueLabel.setText(Integer.toString(jRangeSlider.getHighValue()));
            }
        });
        rangePanel.add(jRangeSlider, BorderLayout.CENTER);

        JPanel highPanel = new JPanel();
        JLabel highTextLabel = new JLabel("Max:");
        highPanel.add(highTextLabel);
        highValueLabel = new JLabel("0");
        highPanel.add(highValueLabel);
        rangePanel.add(highPanel, BorderLayout.EAST);

        quantifiedTargetsPanel.add(rangePanel);

        JLabel ofLabel = new JLabel("of");
        quantifiedTargetsPanel.add(ofLabel);

        targetPicker = new JTargetPicker(new TargetClickListener());
        quantifiedTargetsPanel.add(targetPicker);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 10, 5, 5);
        dialog.add(quantifiedTargetsPanel, constraints);

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
                setText(getICondition().toString());
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

    private class TargetClickListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int currentMax = (Integer)(jRangeSlider.getMaximum());
            int newMax = ((JToggleButton)e.getSource()).isSelected() ? currentMax + 1 : currentMax - 1;
            jRangeSlider.setMaximum(newMax);
            if (jRangeSlider.getHighValue() == currentMax) {
                jRangeSlider.setHighValue(newMax);
            }
            if (jRangeSlider.getLowValue() == currentMax) {
                jRangeSlider.setLowValue(newMax);
            }
            jRangeSlider.revalidate();
        }
    }
}
