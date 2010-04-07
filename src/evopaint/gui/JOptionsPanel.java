/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.Configuration;
import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import evopaint.commands.SelectCommand;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author tam
 */
public class JOptionsPanel extends JPanel {
    private Configuration configuration;

    public void displayOptions(Class toolClass) {
        if (toolClass == MoveCommand.class) {
            ((CardLayout)getLayout()).show(this, "empty");
            return;
        }

        if (toolClass == PaintCommand.class) {
            ((CardLayout)getLayout()).show(this, "paint");
            return;
        }

        if (toolClass == SelectCommand.class) {
            ((CardLayout)getLayout()).show(this, "empty");
            return;
        }
    }

    public JOptionsPanel(Configuration configuration) {
        this.configuration = configuration;
        setLayout(new CardLayout());

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(0xF2F2F5));
        add(emptyPanel, "empty");

        BrushOptionsPanel paintOptionsPanel = new BrushOptionsPanel(configuration);
        add(paintOptionsPanel, "paint");
    }
}
