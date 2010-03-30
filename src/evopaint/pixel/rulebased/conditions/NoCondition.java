/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.World;
import evopaint.gui.ruleseteditor.util.NamedObjectListCellRenderer;
import evopaint.pixel.rulebased.util.ObjectComparisonOperator;
import evopaint.pixel.rulebased.AbstractCondition;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComboBox;
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
        return "the sun is shining";
    }

    public boolean isMet(Pixel us, World world) {
        return true;
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI() {
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
