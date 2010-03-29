/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public interface IAction extends INamed {
    public int getCost();
    public List<RelativeCoordinate> getDirections();
    public int execute(Pixel actor, World world);
    LinkedHashMap<String,JComponent> getParametersForGUI();
}
