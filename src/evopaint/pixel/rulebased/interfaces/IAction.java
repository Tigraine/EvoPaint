/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.Configuration;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public interface IAction extends INamed, IHTML, Serializable {
    public int getCost();
    public void setCost(int cost);
    public List<RelativeCoordinate> getDirections();
    public int execute(Pixel actor, World world);
    LinkedHashMap<String, JComponent> parametersCallbackGUI(LinkedHashMap<String, JComponent> parametersMap);
}
