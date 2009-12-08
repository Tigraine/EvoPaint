/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.attributes.SpacialAttribute;
import evopaint.entities.World;
import evopaint.interfaces.IRandomNumberGenerator;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author tam
 */
public class Relator extends Thread {

    List<Relation> myShare;
    IRandomNumberGenerator rng;

    @Override
    public void run() {
        // relate anything related (pun intended)
        for (Relation relation : this.myShare) {
            if (!relation.relate(this.rng)) {
                if (Config.oneRelationPerEntity == true) {
                    relation.resetB(rng);
                } else {
                    relation.reset(rng);
                }
            }
        }
    }

    public Relator(List<Relation> myShare, IRandomNumberGenerator rng) {
        this.myShare = myShare;
        this.rng = rng;
    }
}
