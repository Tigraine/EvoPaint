/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.actions;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.interfaces.IAction;
import evopaint.util.Color;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public class AssimilationAction extends AbstractAction implements IAction {

    private RelativeCoordinate direction;
    private int colorMixMode;

    public RelativeCoordinate getDirection() {
        return direction;
    }

    public void setDirection(RelativeCoordinate direction) {
        this.direction = direction;
    }

    public int getColorMixMode() {
        return colorMixMode;
    }

    public void setColorMixMode(int colorMixMode) {
        this.colorMixMode = colorMixMode;
    }
    
    public int execute(Pixel us, World world) {
        Pixel them = us.getNeighbor(world, direction);
        
        if (them == null) {
            them = new Pixel(world.getConfiguration().startingEnergy, new Color(0xFF000000), us.getLocation().getNeighboring(direction, world));
            world.add(them);
        }

        them.getColor().mixIn(us.getColor(), 0.5f, colorMixMode);

        return (-1) * cost;
    }

    public AssimilationAction(int cost, RelativeCoordinate direction, int colorMixMode) {
        super(cost, "assimilate " + direction);
        this.direction = direction;
        this.colorMixMode = colorMixMode;
    }
}
