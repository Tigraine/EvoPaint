/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.attributes;

import evopaint.interfaces.IAttribute;


/**
 *
 * @author tam
 */
public class NeuronalAttribute implements IAttribute {

    private short threshold;
    private byte index;

    public byte getIndex() {
        return index;
    }

    public short getThreshold() {
        return threshold;
    }

    public NeuronalAttribute(short threshold, byte index) {
        this.threshold = threshold;
        this.index = index;
    }
}
