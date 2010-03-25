/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.actions;

import evopaint.pixel.AbstractAction;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author tam
 */
public class AssimilationAction extends AbstractAction {

    private int dimensionsToMix;

    @Override
    public String toString() {
        String ret = "assimilate(";
        ret += "in dimensions: [";
        switch (dimensionsToMix) {
            case PixelColor.HSB: ret += "H,S,B"; break;
            case PixelColor.H: ret += "H"; break;
            case PixelColor.S: ret += "S"; break;
            case PixelColor.B: ret += "B"; break;
            case PixelColor.HS: ret += "H,S"; break;
            case PixelColor.HB: ret += "H,B"; break;
            case PixelColor.SB: ret += "S,B"; break;
            default: assert(false);
        }
        ret += "], ";
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

    public AssimilationAction(int reward, List<RelativeCoordinate> directions, int colorMixMode) {
        super(reward, directions);
        this.dimensionsToMix = colorMixMode;
    }
}
