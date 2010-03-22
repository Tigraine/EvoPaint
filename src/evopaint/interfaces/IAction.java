/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;

/**
 *
 * @author tam
 */
public interface IAction {
    public int getReward();
    public int execute(Pixel pixel, RelativeCoordinate direction, World world);
}
