/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.util;

import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.logging.Logger;
import java.io.Serializable;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author tam
 */
public class NumberComparisonOperator implements INamed, Serializable {
    private static final int TYPE_EQUAL = 0;
    private static final int TYPE_NOT_EQUAL = 1;
    private static final int TYPE_GREATER_THAN = 2;
    private static final int TYPE_LESS_THAN = 3;
    private static final int TYPE_GREATER_OR_EQUAL = 4;
    private static final int TYPE_LESS_OR_EQUAL = 5;

    public static final NumberComparisonOperator EQUAL = new NumberComparisonOperator(TYPE_EQUAL);
    public static final NumberComparisonOperator NOT_EQUAL = new NumberComparisonOperator(TYPE_NOT_EQUAL);
    public static final NumberComparisonOperator GREATER_THAN = new NumberComparisonOperator(TYPE_GREATER_THAN);
    public static final NumberComparisonOperator LESS_THAN = new NumberComparisonOperator(TYPE_LESS_THAN);
    public static final NumberComparisonOperator GREATER_OR_EQUAL = new NumberComparisonOperator(TYPE_GREATER_OR_EQUAL);
    public static final NumberComparisonOperator LESS_OR_EQUAL = new NumberComparisonOperator(TYPE_LESS_OR_EQUAL);

    private int type;
   
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        switch (this.type) {
            case TYPE_EQUAL: return "==";
            case TYPE_NOT_EQUAL: return "!=";
            case TYPE_GREATER_THAN: return ">";
            case TYPE_LESS_THAN: return "<";
            case TYPE_GREATER_OR_EQUAL: return ">=";
            case TYPE_LESS_OR_EQUAL: return "<=";
            default: assert(false);
        }
        return null;
    }

    public boolean compare(double a, double b) {
        switch (this.type) {
            case TYPE_EQUAL: return a == b;
            case TYPE_NOT_EQUAL: return a != b;
            case TYPE_GREATER_THAN: return a > b;
            case TYPE_LESS_THAN: return a < b;
            case TYPE_GREATER_OR_EQUAL: return a >= b;
            case TYPE_LESS_OR_EQUAL: return a <= b;
            default: assert(false);
        }
        return false;
    }

    public static DefaultComboBoxModel createComboBoxModel() {
        DefaultComboBoxModel ret = new DefaultComboBoxModel();
        ret.addElement(EQUAL);
        ret.addElement(NOT_EQUAL);
        ret.addElement(GREATER_THAN);
        ret.addElement(LESS_THAN);
        ret.addElement(GREATER_OR_EQUAL);
        ret.addElement(LESS_OR_EQUAL);
        return ret;
    }

    // preserve singleton through serialization
    public Object readResolve() {
        switch (this.type) {
            case TYPE_EQUAL: return NumberComparisonOperator.EQUAL;
            case TYPE_NOT_EQUAL: return NumberComparisonOperator.NOT_EQUAL;
            case TYPE_GREATER_THAN: return NumberComparisonOperator.GREATER_THAN;
            case TYPE_LESS_THAN: return NumberComparisonOperator.LESS_THAN;
            case TYPE_GREATER_OR_EQUAL: return NumberComparisonOperator.GREATER_OR_EQUAL;
            case TYPE_LESS_OR_EQUAL: return NumberComparisonOperator.LESS_OR_EQUAL;
            default: assert(false);
        }
        return null;
    }

    private NumberComparisonOperator(int type) {
        this.type = type;
    }
}
