/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tam
 */
public abstract class AbstractCondition implements ICondition, INamed, IHTML {

    private List<RelativeCoordinate> directions;
    
    public String getDirectionsString() {
        String ret = "[";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        return ret;
    }

    @Override
    public String toString() {
        assert(false);
        return null;
    }

    public String toHTML() {
        assert(false);
        return null;
    }

    public abstract boolean isMet(Pixel us, World world);

    public abstract String getName();
    
    public List<RelativeCoordinate> getDirections() {
        return directions;
    }

    public void setDirections(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

    public AbstractCondition(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }
}
