/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.ColorChooserLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author tam
 */
public class JPixelPanel extends JPanel {
    private Configuration configuration;
    JButton buttonRuleSet;
    JLabel ruleSetNameLabel;

    public void setRuleSetName(String name) {
        this.buttonRuleSet.setText("\"" + name + "\"");
    }

    public JPixelPanel(Configuration configuration, ActionListener openRuleSetManagerListener) {
        setLayout(new BorderLayout());
        setBackground(new Color(0xF2F2F5));

        JLabel brushLabel = new JLabel("<html><b>Pixel</b></html>");
        brushLabel.setBorder(new LineBorder(new Color(0xF2F2F5), 3));
        add(brushLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(0xF2F2F5));

        ColorChooserLabel colorChooserLabel =
                new ColorChooserLabel(configuration.brush.getColor(), configuration.rng);
        JPanel colorChooserAlignmentPanel = new JPanel();
        colorChooserAlignmentPanel.setBackground(new Color(0xF2F2F5));
        colorChooserAlignmentPanel.add(colorChooserLabel);
        contentPanel.add(colorChooserAlignmentPanel);

        buttonRuleSet = new JButton("<No Rule Set>");
        buttonRuleSet.addActionListener(openRuleSetManagerListener);
        JPanel buttonAlignmentPanel = new JPanel();
        buttonAlignmentPanel.setBackground(new Color(0xF2F2F5));
        buttonAlignmentPanel.add(buttonRuleSet);
        contentPanel.add(buttonAlignmentPanel);

        add(contentPanel, BorderLayout.CENTER);
    }

}
