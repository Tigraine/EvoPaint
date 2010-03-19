/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.requirements;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.interfaces.IRequirement;
import evopaint.util.Color;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class LikeColorRequirement extends AbstractRequirement implements IRequirement {
    
    private Color desiredColor;
    private double minLikeliness;
    int colorCompareMode;

    public int getColorCompareMode() {
        return colorCompareMode;
    }

    public void setColorCompareMode(int colorCompareMode) {
        this.colorCompareMode = colorCompareMode;
    }

    public Color getDesiredColor() {
        return desiredColor;
    }

    public void setDesiredColor(Color desiredColor) {
        this.desiredColor = desiredColor;
    }

    public double getMinLikeliness() {
        return minLikeliness;
    }

    public void setMinLikeliness(double minLikeliness) {
        this.minLikeliness = minLikeliness;
    }

    public boolean isMet(Pixel us, World world) {
        Pixel them = world.get(us.getLocation(), direction);
        if (them == null) {
            return false;
        }
        Color theirColor = them.getColor();
        return (desiredColor.isLike(theirColor, minLikeliness, colorCompareMode));
    }

    public LikeColorRequirement(String name, RelativeCoordinate direction, Color desiredColor,
            double minLikeliness, int colorCompareMode) {
        super(name, direction);
        this.desiredColor = desiredColor;
        this.minLikeliness = minLikeliness;
        this.colorCompareMode = colorCompareMode;
    }
}
