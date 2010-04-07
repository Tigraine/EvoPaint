/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.Configuration;
import evopaint.Paint;
import evopaint.pixel.PixelColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class PaintPanel extends JPanel {
    private Configuration configuration;
    private JButton ruleSetButton;
    private JRadioButton colorRadio;
    private JRadioButton fairyDustRadio;
    private JRadioButton useExistingRadio;
    private JButton editColorBtn;
    private JColorChooser colorChooser;

    public void setRuleSetName(String name) {
        this.ruleSetButton.setText("\"" + name + "\"");
    }

    public PaintPanel(final Configuration configuration, ActionListener openRuleSetManagerListener) {
        this.configuration = configuration;

        setLayout(new GridBagLayout());
        setBackground(new Color(0xF2F2F5));
        setBorder(null);
        
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.anchor = GridBagConstraints.NORTHWEST;
        mainConstraints.fill = GridBagConstraints.HORIZONTAL;
        

        this.colorChooser = new JColorChooser();

        JLabel pixelColorLabel = new JLabel("<html><b>Color</b></html>");
        mainConstraints.insets = new Insets(5, 5, 0, 0);
        add(pixelColorLabel, mainConstraints);


        // ---- begin panel for color buttons

        JPanel panelForColorButtons = new JPanel();
        panelForColorButtons.setBackground(new Color(0xF2F2F5));
        panelForColorButtons.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel radioColorPanel = new JPanel();
        radioColorPanel.setBackground(getBackground());
        radioColorPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // color button
        colorRadio = new JRadioButton();
        colorRadio.setText("<html>" + configuration.paint.getColor().toHTML() + "</html>");
        colorRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint = new Paint(Paint.COLOR,
                        configuration.paint.getColor(),
                        configuration.paint.getRuleSet());
            }
        });
        colorRadio.setSelected(true);
        radioColorPanel.add(colorRadio, c);

        editColorBtn = new JButton(new ImageIcon(getClass().getResource("icons/editPencil.png")));
        editColorBtn.setPreferredSize(new Dimension(24, 24));
        editColorBtn.addActionListener(new EditColorListener());
        c.gridx = 1;
        c.insets = new Insets(0, 5, 0, 0);
        radioColorPanel.add(editColorBtn, c);

        panelForColorButtons.add(radioColorPanel, constraints);

        // fairy dust button
        fairyDustRadio = new JRadioButton("Fairy Dust");
        fairyDustRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint = new Paint(Paint.FAIRY_DUST,
                        configuration.paint.getColor(),
                        configuration.paint.getRuleSet());
            }
        });
        constraints.gridy = 1;
        panelForColorButtons.add(fairyDustRadio, constraints);

        // use existing button
        useExistingRadio = new JRadioButton("Use Existing");
        useExistingRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configuration.paint = new Paint(Paint.USE_EXISTING,
                        configuration.paint.getColor(),
                        configuration.paint.getRuleSet());
            }
        });
        constraints.gridy = 2;
        constraints.ipady = 6;
        panelForColorButtons.add(useExistingRadio, constraints);

        ButtonGroup group = new ButtonGroup();
        group.add(colorRadio);
        group.add(fairyDustRadio);
        group.add(useExistingRadio);

        mainConstraints.gridy = 1;
        add(panelForColorButtons, mainConstraints);

        // ---- end panel for color buttons


        JLabel ruleSetButtonLabel = new JLabel("<html><b>Rule Set</b></html>");
        mainConstraints.gridy = 2;
        mainConstraints.insets = new Insets(10, 5, 0, 0);
        add(ruleSetButtonLabel, mainConstraints);


        ruleSetButton = new JButton("<No Rule Set>");
        ruleSetButton.addActionListener(openRuleSetManagerListener);
        JPanel buttonAlignmentPanel = new JPanel();
        buttonAlignmentPanel.setBackground(new Color(0xF2F2F5));
        buttonAlignmentPanel.add(ruleSetButton);

        mainConstraints.gridy = 3;
        mainConstraints.insets = new Insets(0, 0, 0, 0);
        add(buttonAlignmentPanel, mainConstraints);
    }

    public void setPaint(Paint paint) {
        switch (paint.getMode()) {
            case Paint.COLOR: colorRadio.setSelected(true);
            break;
            case Paint.FAIRY_DUST: fairyDustRadio.setSelected(true);
            break;
            case Paint.USE_EXISTING: useExistingRadio.setSelected(true);
            break;
            default: assert(false);
        }
        setRuleSetName(paint.getRuleSet().getName());
    }

    private class EditColorListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            colorChooser.setColor(configuration.paint.getColor().getInteger());
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
            configuration.paint = new Paint(configuration.paint.getMode(),
                        new PixelColor(c.getRGB()),
                        configuration.paint.getRuleSet());
            colorRadio.setText("<html>" + configuration.paint.getColor().toHTML() + "</html>");
        }
    }

    private class ColorChooserCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // NOOP
        }
    }
}
