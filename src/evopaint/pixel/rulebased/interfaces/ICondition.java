/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;

/**
 *
 * @author tam
 */
public interface ICondition {
    public String getName();
    public boolean isMet(Pixel us, World world);
}
