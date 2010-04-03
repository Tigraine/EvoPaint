package evopaint.gui;

import evopaint.Brush;
import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import java.awt.Checkbox;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaintOptionsPanel extends JPanel {

    private JRadioButton radioColor;
    private Configuration configuration;
    Checkbox checkboxRandom;

    public PaintOptionsPanel(Configuration configuration) {

        this.configuration = configuration;

        // here we go, this is going to be one butt-ugly long constructor
        //setBorder(new TitledBorder("Paint Options"));
        setLayout(new GridBagLayout());
        setBackground(new Color(0xF2F2F5));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        JLabel headingLabel = new JLabel("<html><b>Paint Options</b></html>");
        headingLabel.setBorder(new LineBorder(getBackground(), 3));
        add(headingLabel, constraints);
        constraints.insets = new Insets(0, 5, 5, 5);

        // on wich our color stuff panel will reside
        constraints.gridy = 1;
        JPanel panelForColor = new JPanel();
        panelForColor.setBackground(new Color(0xF2F2F5));
        add(panelForColor, constraints);

        // on which we have a labeled panel
        JLabel labelforPanelForColorButtons = new JLabel("Color");
        panelForColor.add(labelforPanelForColorButtons);

        JPanel panelForColorButtons = new JPanel();
        panelForColorButtons.setBackground(new Color(0xF2F2F5));
        panelForColorButtons.setLayout(new GridLayout(3, 1));
        panelForColor.add(panelForColorButtons);       

        // on that we add our two buttons, the one for the color chooser
        radioColor = new JRadioButton("Selected");
        radioColor.addActionListener(new RadioColorListener());
        radioColor.setSelected(true);
        this.configuration.brush.setMode(Brush.COLOR);
        panelForColorButtons.add(radioColor);

        // and the one for the fairy dust
        JRadioButton radioFairyDust = new JRadioButton("Fairy Dust");
        radioFairyDust.addActionListener(new RadioFairyDustListener());
        panelForColorButtons.add(radioFairyDust);
        
        // and the invisible bicycle one
        JRadioButton radioUseExisting = new JRadioButton("Use Existing");
        radioUseExisting.addActionListener(new RadioUseExistingListener());
        panelForColorButtons.add(radioUseExisting);

        ButtonGroup group = new ButtonGroup();
        group.add(radioColor);
        group.add(radioFairyDust);
        group.add(radioUseExisting);

        // and here comes the spinner
        constraints.gridy = 2;
        JPanel panelBrushSize = new JPanel();
        panelBrushSize.setBackground(new Color(0xF2F2F5));
        add(panelBrushSize, constraints);
        
        // labeled
        JLabel labelForSpinner = new JLabel("Size");
        panelBrushSize.add(labelForSpinner);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(configuration.brush.getBrushSize(), 1, 100, 1);
        JSpinner spinnerBrushSize = new AutoSelectOnFocusSpinner(spinnerModel);
        spinnerBrushSize.addChangeListener(new SpinnerBrushSizeListener(spinnerBrushSize));
        labelForSpinner.setLabelFor(spinnerBrushSize);
        panelBrushSize.add(spinnerBrushSize);
    }

    private class RadioColorListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            configuration.brush.setMode(Brush.COLOR);
        }
    }

    private class RadioFairyDustListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            configuration.brush.setMode(Brush.FAIRY_DUST);
        }
    }

    private class RadioUseExistingListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            configuration.brush.setMode(Brush.USE_EXISTING);
        }
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

