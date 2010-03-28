/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tam
 */
public class AssimilationAction extends AbstractAction {

    private ColorDimensions dimensionsToMix;

    public String getName() {
        return "assimilate";
    }

    public ColorDimensions getDimensionsToMix() {
        return dimensionsToMix;
    }

    public void setDimensionsToMix(ColorDimensions dimensionsToMix) {
        this.dimensionsToMix = dimensionsToMix;
    }

    @Override
    public String toString() {
        String ret = "assimilate(";
        ret += "in dimensions: ";
        ret += dimensionsToMix;
        ret += ", ";
        ret += super.toString();
        return ret;
    }

    public int execute(Pixel us, World world) {

        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            them.getPixelColor().mixWith(us.getPixelColor(), 0.5f, dimensionsToMix);
        }

        return getCost() * getDirections().size();
    }

    public AssimilationAction(int reward, List<RelativeCoordinate> directions, ColorDimensions dimensionsToMix) {
        super(reward, directions);
        this.dimensionsToMix = dimensionsToMix;
    }

    public AssimilationAction() {
        super(0, new ArrayList<RelativeCoordinate>(9));
        this.dimensionsToMix = new ColorDimensions(true, true, true);
    }
}
