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

    private String name;
    private int min;
    private int max;
    private List<RelativeCoordinate> directions;

    public AbstractCondition(String name, int min, int max, List<RelativeCoordinate> directions) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.directions = directions;
        assert (min >= 0 && min <= directions.size());
        assert (max >= 0 && max <= directions.size());
    }

    public AbstractCondition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMax() {
        assert (max >= 0 && max <= directions.size());
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        assert (max >= 0 && max <= directions.size());
    }

    public int getMin() {
        assert (min >= 0 && min <= directions.size());
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        assert (min >= 0 && min <= directions.size());
    }

    public List<RelativeCoordinate> getDirections() {
        return directions;
    }

    public void setDirections(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += createDirectionsString();
        ret += " ";
        ret = toStringCallback(ret);
        return ret;
    }

    public String toHTML() {
        String ret = new String();
        ret += createDirectionsString();
        ret += " ";
        ret = toHTMLCallback(ret);
        return ret;
    }

    private String createDirectionsString() {
        String ret = new String();
        if (min == max) {
            if (min == directions.size()) {
                ret += "all";
            }
            else if (max == 0) {
                ret += "none";
            }
            else {
                ret += min;
            }
        }
        else if (min == 0) {
            ret += "up to " + max;
        }
        else if (max == directions.size()) {
            ret += "at least " + min;
        }
        else {
            ret += min + " to " + max;
        }
        ret += " of [";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        return ret;
    }

    public boolean isMet(Pixel origin, World world) {
        int metCounter = 0;
        for (RelativeCoordinate direction : directions) {
            Pixel target = world.get(origin.getLocation(), direction);
            if (isMetCallback(origin, target)) {
                metCounter++;
                if (metCounter >= min && max == directions.size()) {
                    return true;
                } else if (metCounter > max) {
                    return false;
                }
            }
        }
        if (metCounter < min) {
            return false;
        }
        return true;
    }

    /**
     * Children of this class shall implement this callback and it shall return
     * true if the concrete condition is met for the acting pixel if required
     * with respect to another pixel
     * @param us the acting pixel
     * @param them the pixel being checked
     * @return true of the condition holds true, false otherwise
     */
    protected abstract boolean isMetCallback(Pixel us, Pixel them);

    protected abstract String toStringCallback(String conditionString);

    protected abstract String toHTMLCallback(String conditionString);
}
