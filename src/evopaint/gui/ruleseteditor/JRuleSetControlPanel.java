/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author tam
 */
public class JRuleSetControlPanel extends JPanel {

    public JRuleSetControlPanel() {
        setLayout(new GridLayout(0, 1));
        add(new JButton("Load"));
        add(new JButton("Copy"));
        add(new JButton("Name"));
        add(new JButton("Describe"));
        add(new JButton("Delete"));
        add(Box.createVerticalStrut(10));
        add(new JButton("Save"));
        add(Box.createVerticalStrut(10));
        add(new JButton("Use"));
        add(new JButton("Save & Use"));
        add(new JButton("Cancel"));
    }

}
