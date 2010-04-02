/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager.util;

import evopaint.util.mapping.RelativeCoordinate;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author tam
 */
public class TargetsComboBoxModel extends DefaultComboBoxModel {

    public TargetsComboBoxModel() {
        addElement(RelativeCoordinate.SELF);
        addElement(RelativeCoordinate.NORTH);
        addElement(RelativeCoordinate.NORTH_EAST);
        addElement(RelativeCoordinate.EAST);
        addElement(RelativeCoordinate.SOUTH_EAST);
        addElement(RelativeCoordinate.SOUTH);
        addElement(RelativeCoordinate.SOUTH_WEST);
        addElement(RelativeCoordinate.WEST);
        addElement(RelativeCoordinate.NORTH_WEST);
    }

}
