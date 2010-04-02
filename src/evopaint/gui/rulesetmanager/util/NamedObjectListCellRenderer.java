/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager.util;

import evopaint.pixel.rulebased.interfaces.INamed;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.UIResource;

/**
 *
 * @author tam
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

        JLabel ret = (JLabel)super.getListCellRendererComponent(list, " " + ((INamed)value).getName(), index, isSelected, cellHasFocus);

        // #89393: GTK needs name to render cell renderer "natively"
        ret.setName("ComboBox.listRenderer"); // NOI18N
        ret.setPreferredSize(new Dimension(0,25));

        return ret;
    }
}
