/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.Configuration;
import evopaint.Paint;
import evopaint.interfaces.IPaintChangeListener;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleSet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author tam
 */
public class PaintOptionsPanel extends JPanel implements IPaintChangeListener {
    private Configuration configuration;
    private JRadioButton colorRadio;
    private JRadioButton fairyDustRadio;
    private JRadioButton useExistingColorRadio;
    private JRadioButton ruleSetRadio;
    private JRadioButton noRuleSetRadio;
    private JRadioButton useExistingRuleSetRadio;
    private JButton editColorBtn;
    private JButton editRuleSetBtn;
    private JColorChooser colorChooser;
    private RuleSet cachedRuleSet;

    public void paintChanged() {
        switch (configuration.paint.getCurrentColorMode()) {
            case Paint.COLOR: colorRadio.setSelected(true);
                colorRadio.setText("<html>" + configuration.paint.getCurrentColor().toHTML() + "</html>");
            break;
            case Paint.FAIRY_DUST: fairyDustRadio.setSelected(true);
            break;
            case Paint.EXISTING_COLOR: useExistingColorRadio.setSelected(true);
            break;
            default: assert(false);
        }
        switch (configuration.paint.getCurrentRuleSetMode()) {
            case Paint.RULE_SET: ruleSetRadio.setSelected(true);
                ruleSetRadio.setText(configuration.paint.getCurrentRuleSet().getName());
            break;
            case Paint.NO_RULE_SET: noRuleSetRadio.setSelected(true);
            break;
            case Paint.EXISTING_RULE_SET: useExistingRuleSetRadio.setSelected(true);
            break;
            default: assert(false);
        }
    }


    
    public PaintOptionsPanel(final Configuration configuration, ActionListener openRuleSetManagerListener) {
        this.configuration = configuration;

        this.configuration.paint.subscribe(this);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 0, 5);
        setBackground(new Color(0xF2F2F5));
        setBorder(null);
        
        this.colorChooser = new JColorChooser();

        JLabel pixelColorLabel = new JLabel("<html><b>Color</b></html>");
        constraints.gridy = 0;
        add(pixelColorLabel, constraints);


        // ---- begin panel for color buttons

        JPanel panelForColorButtons = new JPanel();
        panelForColorButtons.setBackground(new Color(0xF2F2F5));
        panelForColorButtons.setLayout(new BoxLayout(panelForColorButtons, BoxLayout.Y_AXIS));

        JPanel radioColorPanel = new JPanel();
        radioColorPanel.setBackground(getBackground());
        radioColorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // color button
        colorRadio = new JRadioButton();
        colorRadio.setText("<html>" + configuration.paint.getCurrentColor().toHTML() + "</html>");
        colorRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint.changeCurrentColorMode(Paint.COLOR);
            }
        });
        colorRadio.setSelected(true);
        radioColorPanel.add(colorRadio);

        radioColorPanel.add(Box.createHorizontalStrut(5));

        editColorBtn = new JButton(new ImageIcon(getClass().getResource("icons/button-edit.png")));
        editColorBtn.setPreferredSize(new Dimension(24, 24));
        editColorBtn.addActionListener(new EditColorListener());
        radioColorPanel.add(editColorBtn);

        radioColorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelForColorButtons.add(radioColorPanel);

        // fairy dust button
        fairyDustRadio = new JRadioButton("Fairy Dust");
        fairyDustRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint.changeCurrentColorMode(Paint.FAIRY_DUST);
            }
        });
        fairyDustRadio.setPreferredSize(radioColorPanel.getPreferredSize());
        fairyDustRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelForColorButtons.add(fairyDustRadio);

        // use existing button
        useExistingColorRadio = new JRadioButton("Use Existing");
        useExistingColorRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint.changeCurrentColorMode(Paint.EXISTING_COLOR);
            }
        });
        useExistingColorRadio.setPreferredSize(radioColorPanel.getPreferredSize());
        useExistingColorRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelForColorButtons.add(useExistingColorRadio);

        ButtonGroup group = new ButtonGroup();
        group.add(colorRadio);
        group.add(fairyDustRadio);
        group.add(useExistingColorRadio);

        constraints.gridy = 1;
        add(panelForColorButtons, constraints);

        // ---- end panel for color buttons


        JLabel ruleSetButtonLabel = new JLabel("<html><b>Rule Set</b></html>");
        constraints.gridy = 2;
        constraints.insets = new Insets(15, 5, 0, 5);
        add(ruleSetButtonLabel, constraints);


         // ---- begin panel for rule set buttons

        JPanel panelForRuleSetButtons = new JPanel();
        panelForRuleSetButtons.setBackground(new Color(0xF2F2F5));
        panelForRuleSetButtons.setLayout(new BoxLayout(panelForRuleSetButtons, BoxLayout.Y_AXIS));

        JPanel radioRuleSetPanel = new JPanel();
        radioRuleSetPanel.setBackground(getBackground());
        radioRuleSetPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // rule set radio
        ruleSetRadio = new JRadioButton();
        final String initialRuleSetRadioText = new String("<select me>");
        ruleSetRadio.setText(initialRuleSetRadioText);
        ruleSetRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ruleSetRadio.getText() == initialRuleSetRadioText) { // intentional object comparison
                    return;
                }
                configuration.paint.changeCurrentRuleSetMode(Paint.RULE_SET);
            }
        });
        radioRuleSetPanel.add(ruleSetRadio);

        radioRuleSetPanel.add(Box.createHorizontalStrut(5));

        // rule set redit button
        editRuleSetBtn = new JButton(new ImageIcon(getClass().getResource("icons/button-edit.png")));
        editRuleSetBtn.setPreferredSize(new Dimension(24, 24));
        editRuleSetBtn.addActionListener(openRuleSetManagerListener);
        radioRuleSetPanel.add(editRuleSetBtn);

        radioRuleSetPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelForRuleSetButtons.add(radioRuleSetPanel);

        // no rule set button
        noRuleSetRadio = new JRadioButton("No Rule Set");
        noRuleSetRadio.setSelected(true);
        noRuleSetRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint.changeCurrentRuleSetMode(Paint.NO_RULE_SET);
            }
        });
        noRuleSetRadio.setPreferredSize(radioRuleSetPanel.getPreferredSize());
        noRuleSetRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelForRuleSetButtons.add(noRuleSetRadio);

        // us existing rule set button
        useExistingRuleSetRadio = new JRadioButton("Use Existing");
        useExistingRuleSetRadio.setSelected(true);
        useExistingRuleSetRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint.changeCurrentRuleSetMode(Paint.EXISTING_RULE_SET);
            }
        });
        useExistingRuleSetRadio.setPreferredSize(radioRuleSetPanel.getPreferredSize());
        useExistingRuleSetRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelForRuleSetButtons.add(useExistingRuleSetRadio);

        ButtonGroup group2 = new ButtonGroup();
        group2.add(ruleSetRadio);
        group2.add(noRuleSetRadio);
        group2.add(useExistingRuleSetRadio);

        constraints.gridy = 3;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(panelForRuleSetButtons, constraints);

        // ---- end panel for color buttons
    }

    private class EditColorListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            colorChooser.setColor(configuration.paint.getCurrentColor().getInteger());
            colorChooser.setPreviewPanel(new JPanel());
            JDialog dialog = JColorChooser.createDialog(editColorBtn, "Choose Color", true,
                    colorChooser, new ColorChooserOKListener(), new ColorChooserCancelListener());
            dialog.pack();
            dialog.setVisible(true);
        }

    }

     private class ColorChooserOKListener implements ActionListener {

         public void actionPerformed(ActionEvent e) {
            Color c = colorChooser.getColor();
            configuration.paint.changeCurrentColor(new PixelColor(c.getRGB()));
        }
    }

    private class ColorChooserCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // NOOP
        }
    }
}
