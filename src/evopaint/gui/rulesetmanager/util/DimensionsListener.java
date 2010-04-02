/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager.util;

import evopaint.pixel.ColorDimensions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/**
 *
 * @author tam
 */
public class DimensionsListener implements ActionListener {

    ColorDimensions dimensions;
    private JToggleButton btnH;
    private JToggleButton btnS;
    private JToggleButton btnB;

    public DimensionsListener(ColorDimensions dimensions, JToggleButton btnH, JToggleButton btnS, JToggleButton btnB) {
        this.dimensions = dimensions;
        this.btnH = btnH;
        this.btnS = btnS;
        this.btnB = btnB;
    }

    public void actionPerformed(ActionEvent e) {
        JToggleButton source = (JToggleButton) e.getSource();

        if (source == this.btnH) {
            if (this.btnH.isSelected()) {
                dimensions.hue = true;
            } else {
                dimensions.hue = false;
            }
            return;
        }

        if (source == this.btnS) {
            if (this.btnS.isSelected()) {
                dimensions.saturation = true;
            } else {
                dimensions.saturation = false;
            }
            return;
        }

        if (source == this.btnB) {
            if (this.btnB.isSelected()) {
                dimensions.brightness = true;
            } else {
                dimensions.brightness = false;
            }
            return;
        }
    }
}
