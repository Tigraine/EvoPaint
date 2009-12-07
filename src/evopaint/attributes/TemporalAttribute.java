/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.interfaces.IAttribute;

/**
 *
 * @author tam
 */
public class TemporalAttribute implements IAttribute {
    private long time;

    @Override
    public String toString() {
        return "" + this.time;
    }

    public long getTime() {
        return time;
    }

    public void increaseTime() {
        this.time++;
    }

    public TemporalAttribute(long time) {
        this.time = time;
    }
}
