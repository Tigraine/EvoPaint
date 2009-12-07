/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.util.Log;

/**
 *
 * @author tam
 */
public abstract class Relation {
    protected Entity a;
    protected Entity b;

    public abstract boolean relate();

    @Override
    public String toString() {
        String ret = this.getClass().getSimpleName();

        if (Config.logVerbosity >= Log.Verbosity.VERBOSE) {
            ret += "(" + this.hashCode() + ")";
        }
        
        if  (Config.logVerbosity >= Log.Verbosity.VERBOSE) {
                ret += " A={" + this.a + "}, B={" + this.b + "}";
        }

        return ret;
    }

    public Entity getA() {
        return a;
    }

    public Entity getB() {
        return b;
    }

    public void setA(Entity a) {
        this.a = a;
    }

    public void setB(Entity b) {
        this.b = b;
    }

    public Relation(Entity a, Entity b) {
        this.a = a;
        this.b = b;
    }

    public Relation() {}
}
