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
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ObjectComparisonOperator implements INamed, IHTML, Serializable {
    private static final int TYPE_EQUAL = 0;
    private static final int TYPE_NOT_EQUAL = 1;
    
    public static final ObjectComparisonOperator EQUAL = new ObjectComparisonOperator(TYPE_EQUAL);
    public static final ObjectComparisonOperator NOT_EQUAL = new ObjectComparisonOperator(TYPE_NOT_EQUAL);

    private int type;

    public int getType() {
        return type;
    }

    public String getName() {
        return toString();
    }
    
    @Override
    public String toString() {
        switch (this.type) {
            case TYPE_EQUAL: return "==";
            case TYPE_NOT_EQUAL: return "!=";
        }
        assert(false);
        return null;
    }

    public String toHTML() {
        return toString();
    }

    public boolean compare(Object a, Object b) {
        switch (this.type) {
            case TYPE_EQUAL: return a == b; // this is an ObjectID comparison
            case TYPE_NOT_EQUAL: return a != b; // this is an ObjectID comparison
        }
        assert(false);
        return false;
    }

    public static DefaultComboBoxModel createComboBoxModel() {
        DefaultComboBoxModel ret = new DefaultComboBoxModel();
        ret.addElement(EQUAL);
        ret.addElement(NOT_EQUAL);
        return ret;
    }

    // preserve singleton through serialization
    public Object readResolve() {
        switch (this.type) {
            case TYPE_EQUAL: return ObjectComparisonOperator.EQUAL;
            case TYPE_NOT_EQUAL: return ObjectComparisonOperator.NOT_EQUAL;
        }
        assert false;
        return null;
    }

    public static ObjectComparisonOperator getRandom(IRandomNumberGenerator rng) {
        if (rng.nextBoolean()) {
            return EQUAL;
        } else {
            return NOT_EQUAL;
        }
    }

    public static ObjectComparisonOperator getRandomOtherThan(
            ObjectComparisonOperator oco, IRandomNumberGenerator rng) {
        if (oco == EQUAL) {
            return NOT_EQUAL;
        } else {
            return EQUAL;
        }
    }

    private ObjectComparisonOperator(int type) {
        this.type = type;
    }
}
