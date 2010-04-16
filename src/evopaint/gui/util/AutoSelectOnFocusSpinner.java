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

package evopaint.gui.util;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class AutoSelectOnFocusSpinner extends JSpinner {

    public AutoSelectOnFocusSpinner(SpinnerModel model) {
        super(model);
        final JFormattedTextField spinnerText = ((JSpinner.DefaultEditor)getEditor()).getTextField();
        spinnerText.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() { // only seems to work this way
                        public void run() {
                            spinnerText.selectAll();
                        }
                });
            }
            public void focusLost(FocusEvent e) {}
        });
        // some spinners are getting way too big because Integer.MAX_VALUE
        // can get pretty long, in this case we will truncate it a little to save space
        if (getPreferredSize().width > 90) {
            setPreferredSize(new Dimension(90, getPreferredSize().height));
        }
    }
}
