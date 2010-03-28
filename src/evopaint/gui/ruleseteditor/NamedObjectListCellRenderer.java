/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.interfaces.INamed;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
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
    public String getName() {
        String name = super.getName();
        return name == null ? "ComboBox.renderer" : name;  // NOI18N
    }

    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {

        // #89393: GTK needs name to render cell renderer "natively"
        setName("ComboBox.listRenderer"); // NOI18N

        return super.getListCellRendererComponent(list, ((INamed)value).getName(), index, isSelected, cellHasFocus);
    }
}
