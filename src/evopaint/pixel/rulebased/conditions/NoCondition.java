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
        return "<span style='color: #0000E6;'>true</span>";
    }

    public boolean isMet(Pixel us, World world) {
        return true;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration) {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();
        return ret;
    }

    public NoCondition(List<RelativeCoordinate> directions) {
        super(directions);
    }

    public NoCondition() {
        super(new ArrayList<RelativeCoordinate>(9));
    }
}
