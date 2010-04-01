/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
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
public class NoAction extends AbstractAction {

    public String getName() {
        return "No Action";
    }
    
    @Override
    public String toString() {
        return "do nothing";
    }

    @Override
    public String toHTML() {
        return "<b>do nothing</b>";
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration) {
        return null;
    }

    public int execute(Pixel us, World world) {
        // MEEP MEEEEEEP!
        return 0;
    }

    public NoAction(int cost, List<RelativeCoordinate> directions) {
        super(cost, directions);
    }

    public NoAction() {
        super(0, new ArrayList<RelativeCoordinate>(9));
    }
}
