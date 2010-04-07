package evopaint.gui;

import evopaint.Brush;
import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import java.awt.Checkbox;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BrushOptionsPanel extends JPanel {

    private JRadioButton radioColor;
    private Configuration configuration;
    Checkbox checkboxRandom;

    public BrushOptionsPanel(Configuration configuration) {

        this.configuration = configuration;

        // here we go, this is going to be one butt-ugly long constructor
        //setBorder(new TitledBorder("Paint Options"));
        setLayout(new GridBagLayout());
        setBackground(new Color(0xF2F2F5));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        JLabel headingLabel = new JLabel("<html><b>Brush Options</b></html>");
        constraints.insets = new Insets(5, 5, 0, 0);
        add(headingLabel, constraints);
        constraints.insets = new Insets(0, 5, 5, 5);

        // and here comes the spinner
        constraints.gridy = 2;
        JPanel panelBrushSize = new JPanel();
        panelBrushSize.setBackground(new Color(0xF2F2F5));
        add(panelBrushSize, constraints);
        
        // labeled
        JLabel labelForSpinner = new JLabel("Size");
        panelBrushSize.add(labelForSpinner);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(configuration.brush.getBrushSize(), 1, Integer.MAX_VALUE, 1);
        JSpinner spinnerBrushSize = new AutoSelectOnFocusSpinner(spinnerModel);
        spinnerBrushSize.addChangeListener(new SpinnerBrushSizeListener(spinnerBrushSize));
        labelForSpinner.setLabelFor(spinnerBrushSize);
        panelBrushSize.add(spinnerBrushSize);
    }

    private class SpinnerBrushSizeListener implements ChangeListener {
        JSpinner spinnerBrushSize;

        public SpinnerBrushSizeListener(JSpinner spinnerBrushSize) {
            this.spinnerBrushSize = spinnerBrushSize;
        }

        public void stateChanged(ChangeEvent e) {
            configuration.brush.setBrushSize((Integer)spinnerBrushSize.getValue());
        }
    }
}

