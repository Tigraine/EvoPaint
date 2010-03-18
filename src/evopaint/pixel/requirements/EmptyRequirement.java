/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.requirements;

import evopaint.pixel.Pixel;
import evopaint.interfaces.IRequirement;
import evopaint.util.mapping.ParallaxMap;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class EmptyRequirement extends AbstractRequirement implements IRequirement {
    
    public boolean isMet(Pixel pixie, ParallaxMap<Pixel> map) {
        return map.get(pixie.getLocation().getNeighboring(direction, map)) == null;
    }

    public EmptyRequirement(RelativeCoordinate direction) {
        super("is empty", direction);
    }


}
