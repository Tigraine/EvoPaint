/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.actions;

import evopaint.pixel.misc.ColorMixMode;
import evopaint.pixel.AbstractAction;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.interfaces.IAction;
import evopaint.pixel.PixelColor;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author tam
 */
public class AssimilationAction extends AbstractAction {

    private ColorMixMode colorMixMode;

    @Override
    public String toString() {
        String ret = "assimilate(";
        ret += "mode: " + colorMixMode.toString();
        ret += ", ";
        ret += super.toString();
        return ret;
    }

    public int execute(Pixel us, World world) {
        switch (colorMixMode.getMode()) {
            case ColorMixMode.MODE_RGB:
                for (RelativeCoordinate direction : getDirections()) {
                    Pixel them = world.get(us.getLocation(), direction);
                    them.getPixelColor().mixInRGB(us.getPixelColor(), 0.5f);
                }
                break;
            case ColorMixMode.MODE_HSB:
                for (RelativeCoordinate direction : getDirections()) {
                    Pixel them = world.get(us.getLocation(), direction);
                    them.getPixelColor().mixInHSB(us.getPixelColor(), 0.5f);
                }
                break;
        }
      
        return getCost() * getDirections().size();
    }

    public AssimilationAction(int reward, List<RelativeCoordinate> directions, ColorMixMode colorMixMode) {
        super(reward, directions);
        this.colorMixMode = colorMixMode;
    }
}
