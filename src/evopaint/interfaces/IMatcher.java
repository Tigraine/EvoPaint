/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import evopaint.pixel.Pixel;

/**
 *
 * @author tam
 */
public interface IMatcher {
    public float match(Pixel a, Pixel b);
}
