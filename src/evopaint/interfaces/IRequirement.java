/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import evopaint.pixel.Pixel;
import evopaint.util.mapping.ParallaxMap;

/**
 *
 * @author tam
 */
public interface IRequirement {
    public boolean isMet(Pixel pixel, ParallaxMap<Pixel> map);
}
