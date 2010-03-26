/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.util.logging.Logger;

/**
 *
 * @author tam
 */
public class ObjectComparisonOperator {
    private static final int TYPE_EQUAL = 0;
    private static final int TYPE_NOT_EQUAL = 1;
    
    public static final ObjectComparisonOperator EQUAL = new ObjectComparisonOperator(TYPE_EQUAL, "==");
    public static final ObjectComparisonOperator NOT_EQUAL = new ObjectComparisonOperator(TYPE_NOT_EQUAL, "!=");

    private int type;
    private String name;

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

    private ObjectComparisonOperator(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
