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

package evopaint.gui.rulesetmanager;

import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.rulebased.targeting.IQualifier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JQualifierComboBox extends JComboBox {
    private boolean qualifierEnabled;
    private int selectedQualifierIndex = -1;

    public JQualifierComboBox(IQualifier selected) {
        qualifierEnabled = true;
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (IQualifier qualifier : Configuration.AVAILABLE_QUALIFIERS) {
            model.addElement(qualifier);
        }
        setModel(model);
        setRenderer(new NamedObjectListCellRenderer());
        setSelectedItem(selected);

    }

    @Override
    public void setEnabled(boolean bool) {
        super.setEnabled(bool);
        if (bool == true && qualifierEnabled == false) {
            if (selectedQualifierIndex > -1) {
                setSelectedIndex(selectedQualifierIndex);
            }

        }
        else if (bool == false && qualifierEnabled == true) {
            selectedQualifierIndex = getSelectedIndex();
            setSelectedItem(null); // intentional. the named list renderer can deal with this and will display an empty label this is the cheapest way i could find to produce the desired effect of not displaying anything in the combo box
        }
        qualifierEnabled = bool;
    }

    @Override
    public IQualifier getSelectedItem() {
        return (IQualifier)super.getSelectedItem();
    }


    
}
