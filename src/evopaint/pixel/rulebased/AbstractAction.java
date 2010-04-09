/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.pixel.rulebased.interfaces.ITargetSelection;
import evopaint.pixel.rulebased.targetselections.ExistentTargetSelection;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tam
 */
public abstract class AbstractAction implements IAction, INamed, IHTML {
    public static final int ALL = 0;
    public static final int ONE = 1;

    private String name;
    private int cost;
    private int mode;
    private ITargetSelection targetSelection;

    protected AbstractAction(String name, int cost, int mode, ITargetSelection targetSelection) {
        this.name = name;
        this.cost = cost;
        this.mode = mode;
        this.targetSelection = targetSelection;
    }

    protected AbstractAction(String name) {
        this.name = name;
        this.targetSelection = new ExistentTargetSelection(new ArrayList<RelativeCoordinate>(9));
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public ITargetSelection getTargetSelection() {
        return targetSelection;
    }

    public void setTargetSelection(ITargetSelection targetSelection) {
        this.targetSelection = targetSelection;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += name;

        ret += " (";
        
        ret += "targets: { ";
        if (mode == ONE) {
            ret += "One";
        } else {
            ret += "All";
        }
        ret += " of ";
        ret += targetSelection.toString();
        ret += "}, cost:" + cost + ", ";

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

        ret += "<span style='color: #777777;'>targets:</span> {";
        if (mode == ONE) {
            ret += "One";
        } else {
            ret += "All";
        }
        ret += " of ";
        ret += targetSelection.toHTML();
        ret += "}, <span style='color: #777777;'>cost:</span>" + cost + ", ";

        Map<String, String> parametersMap = this.parametersCallbackString(new HashMap<String, String>());
        for (String parameterName : parametersMap.keySet()) {
            ret += "<span style='color: #777777;'>" + parameterName + ":</span> " +
                    parametersMap.get(parameterName) + ", ";
        }
        
        ret = ret.substring(0, ret.length() - 2);
        ret += ")";

        return ret;
    }

    public int execute(Pixel actor, Configuration configuration) {

        // if the action costs more energy than this pixel got, it dies trying
        if (cost > actor.getEnergy()) {
            return actor.getEnergy();
        }

        if (mode == ALL) {
            synchronized (configuration.world) {
                for (RelativeCoordinate direction : targetSelection.getAllSelectedDirections(actor, configuration)) {
                    executeCallback(actor, direction, configuration.world);
                }
            }
        } else {
            synchronized (configuration.world) {
                RelativeCoordinate rc = targetSelection.getOneSelectedDirection(actor, configuration);
                if (rc != null) {
                    executeCallback(actor, rc, configuration.world);
                }
            }
        }
        return cost;
    }

    public abstract void executeCallback(Pixel origin, RelativeCoordinate direction, World world);

    protected abstract Map<String, String>parametersCallbackString(Map<String, String> parametersMap);

    protected abstract Map<String, String>parametersCallbackHTML(Map<String, String> parametersMap);
}
