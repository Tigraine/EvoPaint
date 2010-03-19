/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.requirements;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.interfaces.IRequirement;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class EmptyRequirement extends AbstractRequirement implements IRequirement {
    
    public boolean isMet(Pixel us, World world) {
        return world.get(us.getLocation(), direction) == null;
    }

    public EmptyRequirement(RelativeCoordinate direction) {
        super("is empty", direction);
    }


}
