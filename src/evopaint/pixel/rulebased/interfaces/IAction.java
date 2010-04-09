/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import java.io.Serializable;
import java.util.LinkedHashMap;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public interface IAction extends INamed, IHTML, Serializable {
    public int getCost();
    public void setCost(int cost);
    public int getMode();
    public void setMode(int mode);
    public ITargetSelection getTargetSelection();
    public void setTargetSelection(ITargetSelection targetSelection);
    public int execute(Pixel actor, Configuration configuration);
    LinkedHashMap<String, JComponent> parametersCallbackGUI(LinkedHashMap<String, JComponent> parametersMap);
}
