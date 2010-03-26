/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.IAction;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<RelativeCoordinate> getDirections() {
        return directions;
    }

    public void setDirections(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

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

    public abstract int execute(Pixel actor, World world);

    public abstract String getName();

    protected AbstractAction(int cost, List<RelativeCoordinate> directions) {
        this.cost = cost;
        this.directions = directions;
    }

    public AbstractAction() {
    }
}
