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
            if (!relation.relate()) {
                this.reset(relation);
            }
        }
    }

    private void reset(Relation relation) {
        // pick random A
        relation.setA(World.locationToEntity((this.rng.nextLocation())));

        // choose B from a's immediate environment
        SpacialAttribute sa = (SpacialAttribute) relation.getA().getAttribute(SpacialAttribute.class);
        assert (sa != null);
        Point newLocation = new Point(sa.getLocation());
        newLocation.translate(Config.randomNumberGenerator.nextPositiveInt(3) - 1,
                Config.randomNumberGenerator.nextPositiveInt(3) - 1);
        relation.setB(World.locationToEntity(newLocation));
    }

    public Relator(List<Relation> myShare, IRandomNumberGenerator rng) {
        this.myShare = myShare;
        this.rng = rng;
    }
}
