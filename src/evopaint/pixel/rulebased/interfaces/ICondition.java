/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;
import java.util.LinkedHashMap;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public interface ICondition extends INamed {
    public boolean isMet(Pixel us, World world);
    public LinkedHashMap<String,JComponent> getParametersForGUI();
}
