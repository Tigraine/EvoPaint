/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.misc;

import evopaint.util.logging.Logger;

/**
 *
 * @author tam
 */
public class IntegerComparisonOperator {
    private static final int TYPE_EQUAL = 0;
    private static final int TYPE_NOT_EQUAL = 1;
    private static final int TYPE_GREATER_THAN = 2;
    private static final int TYPE_LESS_THAN = 3;
    private static final int TYPE_GREATER_OR_EQUAL = 4;
    private static final int TYPE_LESS_OR_EQUAL = 5;

    public static final IntegerComparisonOperator EQUAL = new IntegerComparisonOperator(TYPE_EQUAL, "==");
    public static final IntegerComparisonOperator NOT_EQUAL = new IntegerComparisonOperator(TYPE_NOT_EQUAL, "!=");
    public static final IntegerComparisonOperator GREATER_THAN = new IntegerComparisonOperator(TYPE_GREATER_THAN, ">");
    public static final IntegerComparisonOperator LESS_THAN = new IntegerComparisonOperator(TYPE_LESS_THAN, "<");
    public static final IntegerComparisonOperator GREATER_OR_EQUAL = new IntegerComparisonOperator(TYPE_GREATER_OR_EQUAL, ">=");
    public static final IntegerComparisonOperator LESS_OR_EQUAL = new IntegerComparisonOperator(TYPE_LESS_OR_EQUAL, "<=");

    private int type;
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public boolean compare(int a, int b) {
        switch (this.type) {
            case TYPE_EQUAL: return a == b;
            case TYPE_NOT_EQUAL: return a != b;
            case TYPE_GREATER_THAN: return a > b;
            case TYPE_LESS_THAN: return a < b;
            case TYPE_GREATER_OR_EQUAL: return a >= b;
            case TYPE_LESS_OR_EQUAL: return a <= b;
        }
        Logger.log.error("tried to compare with unknown type", new Object());
        return false;
    }

    private IntegerComparisonOperator(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
