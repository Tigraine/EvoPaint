/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.List;

/**
 *
 * @author tam
 */
public interface IAction {
    public int getCost();
    public List<RelativeCoordinate> getDirections();
    public int execute(Pixel actor, World world);
}
