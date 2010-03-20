package evopaint.gui;

import evopaint.gui.ruleeditor.JRuleEditor;
import evopaint.pixel.RuleSet;
import java.awt.Checkbox;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaintOptionsPanel extends JPanel implements ActionListener {

    private Color color;
    private JButton buttonColor;
    private RuleSet ruleSet;
    private JButton buttonRuleSet;
    private boolean fairyDustTime;
    private int brushsize;
    //JColorChooser colorChooser;
    //JSpinner spinnerBrushSize;
    Showcase showcase;
    MainFrame mainFrame;
    Checkbox checkboxRandom;

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
        add(panelForColor);

        // on which we have a labeled panel
        JLabel labelforPanelForColorButtons = new JLabel("Color");
        panelForColor.add(labelforPanelForColorButtons);

        JPanel panelForColorButtons = new JPanel();
        panelForColorButtons.setLayout(new GridLayout(2, 1));
        panelForColor.add(panelForColorButtons);       

        // on that we add our two buttons, the one for the color chooser
        buttonColor = new JButton("#" +
                Integer.toHexString(this.color.getRGB()).substring(2).toUpperCase());
        buttonColor.setOpaque(true);
        buttonColor.setForeground(this.color);
        buttonColor.addActionListener(this);
        panelForColorButtons.add(buttonColor);

        // and the one for the fairy dust
        JToggleButton buttonToggleFairyDust = new JToggleButton("Fairy Dust");
        buttonToggleFairyDust.addActionListener(new FairyDustButtonListener(this));
        panelForColorButtons.add(buttonToggleFairyDust);

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

    public boolean isFairyDustTime() {
        return fairyDustTime;
    }

    public void setFairyDustTime(boolean fairyDustTime) {
        this.fairyDustTime = fairyDustTime;
        buttonColor.setEnabled(!fairyDustTime);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.buttonColor.setText("#" +
                Integer.toHexString(this.color.getRGB()).substring(2).toUpperCase());
        this.buttonColor.setForeground(this.color);
        mainFrame.pack(); // or else our showcase could get veeery small.. wtf?
    }

    public void actionPerformed(ActionEvent e) {
        JColorChooser colorChooser = new JColorChooser(color);
        colorChooser.setPreviewPanel(new JPanel());
        JDialog dialog = JColorChooser.createDialog(this, "Choose Color", true,
                colorChooser, new ColorChooserOKListener(this, colorChooser), new ColorChooserCancelListener());
        dialog.pack();
        dialog.setVisible(true);
    }

    private class ColorChooserOKListener implements ActionListener {
        PaintOptionsPanel paintOptionsPanel;
        JColorChooser colorChooser;

        public ColorChooserOKListener(PaintOptionsPanel paintOptionsPanel, JColorChooser colorChooser) {
            this.paintOptionsPanel = paintOptionsPanel;
            this.colorChooser = colorChooser;
        }

        public void actionPerformed(ActionEvent e) {
            paintOptionsPanel.setColor(colorChooser.getColor());
        }
    }

    private class ColorChooserCancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // NOOP
        }
    }

    private class FairyDustButtonListener implements ActionListener {
        PaintOptionsPanel paintOptionsPanel;

        public FairyDustButtonListener(PaintOptionsPanel paintOptionsPanel) {
            this.paintOptionsPanel = paintOptionsPanel;
        }

        public void actionPerformed(ActionEvent e) {
            if (paintOptionsPanel.isFairyDustTime()) {
                paintOptionsPanel.setFairyDustTime(false);
            } else {
                paintOptionsPanel.setFairyDustTime(true);
            }
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

