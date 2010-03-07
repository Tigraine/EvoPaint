/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.entities.World;
import evopaint.interfaces.IRandomNumberGenerator;
import java.util.List;

/**
 *
 * @author tam
 */
public class Relator extends Thread {

    private World world;
    private List<Relation> myShare;
    private IRandomNumberGenerator rng;
    private final Config configuration;

    @Override
    public void run() {
        // relate anything related (pun intended)
        for (Relation relation : this.myShare) {
            if (!relation.relate(this.rng)) {
                if (configuration.oneRelationPerEntity == true) {
                    relation.resetB(this.world, this.rng);
                } else {
                    relation.reset(this.world, this.rng);
                }
            }
        }
    }

    public Relator(World world, List<Relation> myShare, IRandomNumberGenerator rng, Config configuration) {
        this.world = world;
        this.myShare = myShare;
        this.rng = rng;
        this.configuration = configuration;
    }
}
