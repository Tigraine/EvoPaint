/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.util;

import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.logging.Logger;
import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author tam
 */
public class ObjectComparisonOperator implements INamed {
    private static final int TYPE_EQUAL = 0;
    private static final int TYPE_NOT_EQUAL = 1;
    
    public static final ObjectComparisonOperator EQUAL = new ObjectComparisonOperator(TYPE_EQUAL, "==");
    public static final ObjectComparisonOperator NOT_EQUAL = new ObjectComparisonOperator(TYPE_NOT_EQUAL, "!=");

    private int type;
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean compare(Object a, Object b) {
        switch (this.type) {
            case TYPE_EQUAL: return a == b; // this is an ObjectID comparison
            case TYPE_NOT_EQUAL: return a != b; // this is an ObjectID comparison
        }
        Logger.log.error("tried to compare with unknown type", new Object());
        return false;
    }

    public static DefaultComboBoxModel createComboBoxModel() {
        DefaultComboBoxModel ret = new DefaultComboBoxModel();
        ret.addElement(EQUAL);
        ret.addElement(NOT_EQUAL);
        return ret;
    }

    private ObjectComparisonOperator(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
