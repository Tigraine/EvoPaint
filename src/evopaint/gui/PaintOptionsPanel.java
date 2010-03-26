package evopaint.gui;

import evopaint.Brush;
import evopaint.Configuration;
import evopaint.gui.ruleeditor.JRuleSetEditor;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleSet;
import java.awt.Checkbox;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaintOptionsPanel extends JPanel {

    //private Color color;
    private JRadioButton radioColor;
    private Color selectedColor;
    private boolean radioColorToggle;
    private RuleSet ruleSet;
    private JButton buttonRuleSet;
    private Configuration configuration;
    Showcase showcase;
    MainFrame mainFrame;
    Checkbox checkboxRandom;

    public PaintOptionsPanel(Configuration configuration, final Showcase showcase, final MainFrame mainFrame) {

        this.configuration = configuration;
        this.mainFrame = mainFrame;
        this.showcase = showcase;

        // here we go, this is going to be one butt-ugly long constructor
        setBorder(new TitledBorder("Paint Options"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // on wich our color stuff panel will reside
        JPanel panelForColor = new JPanel();
        //panelForColor.setLayout(new BoxLayout(panelForColor, BoxLayout.Y_AXIS));
        //panelForColor.setLayout(new GridBagLayout());
        add(panelForColor);

        // on which we have a labeled panel
        JLabel labelforPanelForColorButtons = new JLabel("Color");
        panelForColor.add(labelforPanelForColorButtons);

        JPanel panelForColorButtons = new JPanel();
        panelForColorButtons.setLayout(new GridLayout(3, 1));
        panelForColor.add(panelForColorButtons);       

        // on that we add our two buttons, the one for the color chooser
        radioColor = new JRadioButton("#" +
                Integer.toHexString(configuration.brush.getColor().getInteger()).substring(2).toUpperCase());
        //labelColorString.setOpaque(true);
        radioColor.setForeground(new Color(configuration.brush.getColor().getInteger()));
        radioColor.addActionListener(new RadioColorListener(this));
        radioColor.setSelected(true);
        selectedColor = new Color(configuration.brush.getColor().getInteger());
        radioColorToggle = true;
        this.configuration.brush.setMode(Brush.COLOR);
        panelForColorButtons.add(radioColor);

        // and the one for the fairy dust
        JRadioButton radioFairyDust = new JRadioButton("Fairy Dust");
        radioFairyDust.addActionListener(new RadioFairyDustListener(this));
        panelForColorButtons.add(radioFairyDust);
        
        // and the invisible bicycle one
        JRadioButton radioUseExisting = new JRadioButton("Use Existing");
        radioUseExisting.addActionListener(new RadioUseExistingListener(this));
        panelForColorButtons.add(radioUseExisting);

        ButtonGroup group = new ButtonGroup();
        group.add(radioColor);
        group.add(radioFairyDust);
        group.add(radioUseExisting);

        // and here comes the spinner
        JPanel panelBrushSize = new JPanel();
        add(panelBrushSize);
        
        // labeled
        JLabel labelForSpinner = new JLabel("Size");
        panelBrushSize.add(labelForSpinner);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(configuration.brush.getBrushSize(), 1, 100, 1);
        JSpinner spinnerBrushSize = new JSpinner(spinnerModel);
        spinnerBrushSize.addChangeListener(new SpinnerBrushSizeListener(this, spinnerBrushSize));
        labelForSpinner.setLabelFor(spinnerBrushSize);
        panelBrushSize.add(spinnerBrushSize);

        // aaaand the ruleset
        JPanel panelRuleSet = new JPanel();
        add(panelRuleSet);

        // labeled
        JLabel labelForButtonRuleSet = new JLabel("Rule Set");
        panelRuleSet.add(labelForButtonRuleSet);

        buttonRuleSet = new JButton();
        buttonRuleSet.setText("click for demo");
        buttonRuleSet.addActionListener(new ButtonRuleSetListener());
        panelRuleSet.add(buttonRuleSet);
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void setColor(Color color) {
        selectedColor = color;
        this.radioColor.setText("#" +
                Integer.toHexString(color.getRGB()).substring(2).toUpperCase());
        this.radioColor.setForeground(color);
        //this.labelColorString.setBackground(this.color);
        //float [] hsbvals = new float[3];
        //Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbvals);
        //if (hsbvals[2] > 0.5f) {
        //    this.labelColorString.setForeground(Color.BLACK);
        //} else {
        //    this.labelColorString.setForeground(Color.WHITE);
        //}
        mainFrame.pack(); // or else our showcase could get veeery small.. wtf?
    }

    private class RadioColorListener implements ActionListener {
        private PaintOptionsPanel paintOptionsPanel;

        public RadioColorListener(PaintOptionsPanel paintOptionsPanel) {
            this.paintOptionsPanel = paintOptionsPanel;
        }

        public void actionPerformed(ActionEvent e) {
            configuration.brush.setMode(Brush.COLOR);
            configuration.brush.setColor(new PixelColor(selectedColor.getRGB(), configuration.rng));
            
            if (radioColorToggle == false) {
                radioColorToggle = true;
                return;
            }
            Color color = new Color(configuration.brush.getColor().getInteger());
            JColorChooser colorChooser = new JColorChooser(color);
            colorChooser.setPreviewPanel(new JPanel());
            JDialog dialog = JColorChooser.createDialog(paintOptionsPanel, "Choose Color", true,
                    colorChooser, new ColorChooserOKListener(paintOptionsPanel, colorChooser), new ColorChooserCancelListener());
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private class RadioFairyDustListener implements ActionListener {
        PaintOptionsPanel paintOptionsPanel;

        public RadioFairyDustListener(PaintOptionsPanel paintOptionsPanel) {
            this.paintOptionsPanel = paintOptionsPanel;
        }

        public void actionPerformed(ActionEvent e) {
            paintOptionsPanel.radioColorToggle = false;
            configuration.brush.setMode(Brush.FAIRY_DUST);
        }
    }

    private class RadioUseExistingListener implements ActionListener {
        private PaintOptionsPanel paintOptionsPanel;

        public RadioUseExistingListener(PaintOptionsPanel paintOptionsPanel) {
            this.paintOptionsPanel = paintOptionsPanel;
        }

        public void actionPerformed(ActionEvent e) {
            paintOptionsPanel.radioColorToggle = false;
            configuration.brush.setMode(Brush.USE_EXISTING);
        }
    }

    private class ColorChooserOKListener implements ActionListener {
        PaintOptionsPanel paintOptionsPanel;
        JColorChooser colorChooser;

        public ColorChooserOKListener(PaintOptionsPanel paintOptionsPanel, JColorChooser colorChooser) {
            this.paintOptionsPanel = paintOptionsPanel;
            this.colorChooser = colorChooser;
        }

        public void actionPerformed(ActionEvent e) {
            paintOptionsPanel.radioColorToggle = true;
            Color c = colorChooser.getColor();
            paintOptionsPanel.setColor(c);
            configuration.brush.setColor(new PixelColor(c.getRGB(), configuration.rng));
        }
    }

    private class ColorChooserCancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // NOOP
        }
    }

    private class SpinnerBrushSizeListener implements ChangeListener {
        PaintOptionsPanel paintOptionsPanel;
        JSpinner spinnerBrushSize;

        public SpinnerBrushSizeListener(PaintOptionsPanel paintOptionsPanel, JSpinner spinnerBrushSize) {
            this.paintOptionsPanel = paintOptionsPanel;
            this.spinnerBrushSize = spinnerBrushSize;
        }

        public void stateChanged(ChangeEvent e) {
            configuration.brush.setBrushSize((Integer)spinnerBrushSize.getValue());
        }
    }

    private class ButtonRuleSetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(mainFrame, "Rule Set Editor", true);
            dialog.add(new JRuleSetEditor(configuration.brush.getRuleSet()));
            dialog.pack();
            dialog.setVisible(true);
        }
    }
}

