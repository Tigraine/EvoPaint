/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.actions.ActionWrapper;
import evopaint.pixel.conditions.ConditionWrapper;
import java.util.List;

/**
 *
 * @author tam
 */
public interface IRule {

    public List<ActionWrapper> getActions();

    public List<ConditionWrapper> getConditions();
    
    public boolean apply(Pixel pixel, World world);
}
