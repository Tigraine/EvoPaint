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
public interface ICondition extends INamed, IHTML, Serializable {
    public List<RelativeCoordinate> getDirections();
    public boolean isMet(Pixel us, World world);
    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration);
}
