/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;
import java.util.List;

/**
 *
 * @author tam
 */
public interface IRule {

    public IAction getAction();

    public List<ICondition> getConditions();
    
    public boolean apply(Pixel pixel, World world);
}
