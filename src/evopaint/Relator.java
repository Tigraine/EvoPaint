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
    private List<PixelRelation> myShare;
    private IRandomNumberGenerator rng;
    private final Config configuration;

    @Override
    public void run() {
        // relate anything related (pun intended)
        for (PixelRelation relation : this.myShare) {
            if (!relation.relate(this.world, this.rng)) {
                if (configuration.pixelsAct == true) {
                    relation.resetB(this.world, this.rng);
                } else {
                    relation.reset(this.world, this.rng);
                }
            }
        }
    }

    public Relator(World world, List<PixelRelation> myShare, IRandomNumberGenerator rng, Config configuration) {
        this.world = world;
        this.myShare = myShare;
        this.rng = rng;
        this.configuration = configuration;
    }
}
