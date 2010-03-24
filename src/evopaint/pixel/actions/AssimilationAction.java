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

    private int colorMixMode;

    @Override
    public String toString() {
        String ret = "assimilate(";
        ret += "mode: ";
        switch (colorMixMode) {
            case PixelColor.HSB: ret += "HSB"; break;
            case PixelColor.H: ret += "H"; break;
            case PixelColor.S: ret += "S"; break;
            case PixelColor.B: ret += "B"; break;
            case PixelColor.HS: ret += "HS"; break;
            case PixelColor.SB: ret += "SB"; break;
            case PixelColor.BH: ret += "BH"; break;
            default: assert(false);
        }
        ret += ", ";
        ret += super.toString();
        return ret;
    }

    public int execute(Pixel us, World world) {

        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            them.getPixelColor().setHSB(PixelColor.mix(us.getPixelColor(),
                    them.getPixelColor(), 0.5f, colorMixMode));
        }

        return getCost() * getDirections().size();
    }

    public AssimilationAction(int reward, List<RelativeCoordinate> directions, int colorMixMode) {
        super(reward, directions);
        this.colorMixMode = colorMixMode;
    }
}
