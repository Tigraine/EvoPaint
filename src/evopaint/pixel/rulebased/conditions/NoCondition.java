/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public class NoCondition extends AbstractCondition {

    public NoCondition(int min, int max, List<RelativeCoordinate> directions) {
        super("true", min, max, directions);
    }

    public NoCondition() {
        super("true", 0, 0, new ArrayList<RelativeCoordinate>(9));
    }

    protected boolean isMetCallback(Pixel us, Pixel them) {
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }

    public String toStringCallback(String conditionString) {
        // overwriting toString()
        return null;
    }

    @Override
    public String toHTML() {
        return "true";
    }

    public String toHTMLCallback(String conditionString) {
        // overwriting toHTML()
        return null;
    }

    public LinkedHashMap<String,JComponent> parametersCallbackGUI(LinkedHashMap<String,JComponent> parametersMap) {
        return parametersMap;
    }
}
