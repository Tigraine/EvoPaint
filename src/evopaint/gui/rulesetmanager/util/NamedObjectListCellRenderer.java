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

import evopaint.pixel.rulebased.interfaces.INamed;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.UIResource;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */

public class NamedObjectListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer, UIResource {

    public NamedObjectListCellRenderer() {
    }

    // #89393: GTK needs name to render cell renderer "natively"
    @Override
    public String getName() {
        String name = super.getName();
        return name == null ? "ComboBox.renderer" : name;  // NOI18N
    }

    @Override
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {

        JLabel ret = null;

        if (value != null) {
            ret = (JLabel)super.getListCellRendererComponent(list, " " + ((INamed)value).getName(), index, isSelected, cellHasFocus);
        } else {
            ret = new JLabel();
        }
        
        // #89393: GTK needs name to render cell renderer "natively"
        ret.setName("ComboBox.listRenderer"); // NOI18N

        return ret;
    }
}
