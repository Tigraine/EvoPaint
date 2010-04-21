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

package evopaint.pixel.rulebased.util;

import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.INamed;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class NumberComparisonOperator implements INamed, IHTML, Serializable {
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
    
    public String toHTML() {
        switch (this.type) {
            case TYPE_EQUAL: return "==";
            case TYPE_NOT_EQUAL: return "!=";
            case TYPE_GREATER_THAN: return "&gt;";
            case TYPE_LESS_THAN: return "&lt;";
            case TYPE_GREATER_OR_EQUAL: return "&gt;=";
            case TYPE_LESS_OR_EQUAL: return "&lt;=";
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

    public static NumberComparisonOperator getRandom(IRandomNumberGenerator rng) {
        int rnd = rng.nextPositiveInt(6);
        switch (rnd) {
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

    public static NumberComparisonOperator getRandomOtherThan(
            NumberComparisonOperator nco, IRandomNumberGenerator rng) {

        ArrayList<NumberComparisonOperator> unusedOperators = new ArrayList<NumberComparisonOperator>() {{
            add(EQUAL);
            add(NOT_EQUAL);
            add(GREATER_THAN);
            add(LESS_THAN);
            add(GREATER_OR_EQUAL);
            add(LESS_OR_EQUAL);
        }};
        unusedOperators.remove(nco);
        
        return unusedOperators.get(rng.nextPositiveInt(unusedOperators.size()));
    }

    private NumberComparisonOperator(int type) {
        this.type = type;
    }
}
