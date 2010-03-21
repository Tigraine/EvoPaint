package evopaint.gui;

import evopaint.gui.ruleeditor.JRuleEditor;
import evopaint.pixel.RuleSet;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.Box;
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
    public static final int COLORMODE_COLOR = 0;
    public static final int COLORMODE_FAIRY_DUST = 1;
    public static final int COLORMODE_USE_EXISTING = 2;

    private Color color;
    private JRadioButton radioColor;
    private RuleSet ruleSet;
    private JButton buttonRuleSet;
    private int colorMode;
    private int brushsize;
    private boolean colorSelected;
    //JColorChooser colorChooser;
    //JSpinner spinnerBrushSize;
    Showcase showcase;
    MainFrame mainFrame;
    Checkbox checkboxRandom;

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }

    public PaintOptionsPanel(final Showcase showcase, final MainFrame mainFrame) {

        this.mainFrame = mainFrame;
        this.showcase = showcase;
        this.color = Color.red;
        this.brushsize = 10;

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
                Integer.toHexString(this.color.getRGB()).substring(2).toUpperCase());
        //labelColorString.setOpaque(true);
        radioColor.setForeground(this.color);
        radioColor.addActionListener(new RadioColorListener(this));
        radioColor.setSelected(true);
        colorSelected = true;
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

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(this.brushsize, 1, 100, 1);
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

    public int getBrushsize() {
        return brushsize;
    }

    public void setBrushsize(int brushsize) {
        this.brushsize = brushsize;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.radioColor.setText("#" +
                Integer.toHexString(this.color.getRGB()).substring(2).toUpperCase());
        this.radioColor.setForeground(this.color);
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
            paintOptionsPanel.colorMode = COLORMODE_COLOR;
            
            if (paintOptionsPanel.colorSelected == false) {
                paintOptionsPanel.colorSelected = true;
                return;
            }

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
            paintOptionsPanel.colorSelected = false;
            paintOptionsPanel.colorMode = COLORMODE_FAIRY_DUST;
        }
    }

    private class RadioUseExistingListener implements ActionListener {
        private PaintOptionsPanel paintOptionsPanel;

        public RadioUseExistingListener(PaintOptionsPanel paintOptionsPanel) {
            this.paintOptionsPanel = paintOptionsPanel;
        }

        public void actionPerformed(ActionEvent e) {
            paintOptionsPanel.colorSelected = false;
            paintOptionsPanel.colorMode = COLORMODE_USE_EXISTING;
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
            paintOptionsPanel.colorSelected = true;
            paintOptionsPanel.setColor(colorChooser.getColor());
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
            paintOptionsPanel.setBrushsize((Integer)spinnerBrushSize.getValue());
        }
    }

    private class ButtonRuleSetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(mainFrame, "Rule Set Editor", true);
            dialog.add(new JRuleEditor());
            dialog.pack();
            dialog.setVisible(true);
        }
    }
}

