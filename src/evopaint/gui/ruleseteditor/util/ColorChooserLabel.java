/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor.util;

import evopaint.Configuration;
import evopaint.gui.MainFrame;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.PixelColor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 *
 * @author tam
 */
public class ColorChooserLabel extends JLabel {
    private PixelColor pixelColor;
    private JColorChooser colorChooser;
    private IRandomNumberGenerator rng;

    public ColorChooserLabel(PixelColor pixelColor, IRandomNumberGenerator rng) {
        this.pixelColor = pixelColor;
        this.rng = rng;
        this.colorChooser = new JColorChooser(new Color(pixelColor.getInteger()));
        setText("<html>" + pixelColor.toHTML() + "</html>");
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        addMouseListener(new ClickListener(this));
    }

    private class ClickListener implements MouseListener {
        private ColorChooserLabel colorChooserLabel;

        public ClickListener(ColorChooserLabel colorChooserLabel) {
            this.colorChooserLabel = colorChooserLabel;
        }
        
        public void mouseClicked(MouseEvent e) {
            colorChooser.setColor(pixelColor.getInteger());
            colorChooser.setPreviewPanel(new JPanel());
            JDialog dialog = JColorChooser.createDialog(colorChooserLabel, "Choose Color", true,
                    colorChooser, new ColorChooserOKListener(colorChooserLabel), new ColorChooserCancelListener());
            dialog.pack();
            dialog.setVisible(true);
        }

        public void mousePressed(MouseEvent e) {}

        public void mouseReleased(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

    }

    private class ColorChooserOKListener implements ActionListener {
        private ColorChooserLabel colorChooserLabel;

        public ColorChooserOKListener(ColorChooserLabel colorChooserLabel) {
            this.colorChooserLabel = colorChooserLabel;
        }

        public void actionPerformed(ActionEvent e) {
            Color c = colorChooser.getColor();
            pixelColor.setInteger(c.getRGB(), rng);
            colorChooserLabel.setText("<html>" + pixelColor.toHTML() + "</html>");
        }
    }

    private class ColorChooserCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // NOOP
        }
    }
}
