/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import evopaint.entities.World;

/**
 *
 * @author tam
 */
public interface IRelation {
    public boolean relate(World world, IRandomNumberGenerator rng);
}
