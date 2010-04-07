/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tam
 */
public abstract class AbstractAction implements IAction, INamed, IHTML {

    private String name;
    private int cost;
    private List<RelativeCoordinate> directions;

    protected AbstractAction(String name, int cost, List<RelativeCoordinate> directions) {
        this.name = name;
        this.cost = cost;
        this.directions = directions;
    }

    protected AbstractAction(String name) {
        this.name = name;
        this.directions = new ArrayList<RelativeCoordinate>(9);
    }

    protected AbstractAction() {}

    public String getName() {
        return name;
    }

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
        String ret = new String();
        ret += name;

        ret += " (";
        
        ret += "targets: [";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        ret += ", cost:" + cost + ", ";

        Map<String, String> parametersMap = this.parametersCallbackString(new HashMap<String, String>());
        for (String parameterName : parametersMap.keySet()) {
            ret += parameterName + ": " + parametersMap.get(parameterName) + ", ";
        }

        ret = ret.substring(0, ret.length() - 2);
        ret += ")";

        return ret;
    }

    public String toHTML() {
        String ret = new String();
        ret += "<b>" + name + "</b>";

        ret += " (";

        ret += "<span style='color: #777777;'>targets:</span> [";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        ret += ", <span style='color: #777777;'>cost:</span>" + cost + ", ";

        Map<String, String> parametersMap = this.parametersCallbackString(new HashMap<String, String>());
        for (String parameterName : parametersMap.keySet()) {
            ret += "<span style='color: #777777;'>" + parameterName + ":</span> " +
                    parametersMap.get(parameterName) + ", ";
        }
        
        ret = ret.substring(0, ret.length() - 2);
        ret += ")";

        return ret;
    }

    public int execute(Pixel actor, World world) {

        // if the action costs more energy than this pixel got, it dies trying
        if (cost > actor.getEnergy()) {
            return actor.getEnergy();
        }

        for (RelativeCoordinate target : directions) {
            executeCallback(actor, target, world);
        }

        return cost;
    }

    public abstract void executeCallback(Pixel origin, RelativeCoordinate direction, World world);

    protected abstract Map<String, String>parametersCallbackString(Map<String, String> parametersMap);

    protected abstract Map<String, String>parametersCallbackHTML(Map<String, String> parametersMap);
}
