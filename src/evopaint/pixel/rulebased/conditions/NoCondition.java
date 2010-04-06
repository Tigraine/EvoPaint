/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.World;
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

    public String getName() {
        return "True";
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public String toHTML() {
        return "true";
    }

    @Override
    protected boolean isMetCallback(Pixel us, Pixel them) {
        return true;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration) {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();
        return ret;
    }

    public NoCondition(int min, int max, List<RelativeCoordinate> directions) {
        super(min, max, directions);
    }

    public NoCondition() {
        super(0, 0, new ArrayList<RelativeCoordinate>(9));
    }
}
