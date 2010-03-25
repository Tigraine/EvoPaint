/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.World;
import evopaint.pixel.interfaces.IAction;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tam
 */
public abstract class AbstractAction implements IAction {

    private int cost;
    private List<RelativeCoordinate> directions;

    @Override
    public String toString() {
        String ret = "cost: " + cost + "*" + directions.size() + ") -> [";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += " & ";
            }
        }
        ret += "] ";
        return ret;
    }

    public List<RelativeCoordinate> getDirections() {
        return directions;
    }

    public int getCost() {
        return cost;
    }

    public abstract int execute(Pixel actor, World world);

    protected AbstractAction(int cost, List<RelativeCoordinate> directions) {
        this.cost = cost;
        this.directions = directions;
    }
}
