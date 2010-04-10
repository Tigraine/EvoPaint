/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.gui.rulesetmanager.util;

import evopaint.pixel.PixelColor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ColorChooserLabel extends JLabel {
    private PixelColor pixelColor;
    private JColorChooser colorChooser;

    public ColorChooserLabel(PixelColor pixelColor) {
        this.pixelColor = pixelColor;
        this.colorChooser = new JColorChooser(new Color(pixelColor.getInteger()));
        setText("<html>" + pixelColor.toHTML() + "</html>");
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        addMouseListener(new ClickListener(this));
    }

    private class ClickListener extends MouseAdapter {
        private ColorChooserLabel colorChooserLabel;

        public ClickListener(ColorChooserLabel colorChooserLabel) {
            this.colorChooserLabel = colorChooserLabel;
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            colorChooser.setColor(pixelColor.getInteger());
            colorChooser.setPreviewPanel(new JPanel());
            JDialog dialog = JColorChooser.createDialog(colorChooserLabel, "Choose Color", true,
                    colorChooser, new ColorChooserOKListener(colorChooserLabel), new ColorChooserCancelListener());
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private class ColorChooserOKListener implements ActionListener {
        private ColorChooserLabel colorChooserLabel;

        public ColorChooserOKListener(ColorChooserLabel colorChooserLabel) {
            this.colorChooserLabel = colorChooserLabel;
        }

        public void actionPerformed(ActionEvent e) {
            Color c = colorChooser.getColor();
            pixelColor.setInteger(c.getRGB());
            colorChooserLabel.setText("<html>" + pixelColor.toHTML() + "</html>");
        }
    }

    private class ColorChooserCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // NOOP
        }
    }
}
