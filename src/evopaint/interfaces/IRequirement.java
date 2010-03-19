/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import evopaint.World;
import evopaint.pixel.Pixel;

/**
 *
 * @author tam
 */
public interface IRequirement {
    public boolean isMet(Pixel us, World world);
}
