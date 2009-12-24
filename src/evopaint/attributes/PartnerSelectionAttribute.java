/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.attributes;

import evopaint.Config;
import evopaint.Entity;
import evopaint.entities.World;
import evopaint.interfaces.IAttribute;
import evopaint.interfaces.IMatcher;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.Log;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tam
 */
public class PartnerSelectionAttribute implements IAttribute {
    private IMatcher matcher;
    private float minScore;
    private float maxScore;

    public Entity findPartner(World world, Entity us, int radius, IRandomNumberGenerator rng) {

        // check if we exist in space
        SpacialAttribute sa = (SpacialAttribute) us.getAttribute(SpacialAttribute.class);
        if (sa == null) {
            Config.log.information("cannot find partner (we have no spacial means) %s", this);
            return null;
        }

        // get the possible matches from the environment of our entity
        List<Entity> environment = new ArrayList<Entity>((2*radius+1)*(2*radius+1)-1);
        Point loc = sa.getOrigin();
        for (int y = loc.y - radius; y <= loc.y + radius; y++) {
            for (int x = loc.x - radius; x <= loc.x + radius; x++) {

                // skip self
                if (x == loc.x && y == loc.y) {
                    continue;
                }

                environment.add(world.locationToEntity(new Point(x, y)));
            }
        }

        // scan the environment for good matches
        List<Entity> winners = new ArrayList<Entity>();
        float highestScore = 0.0f;
        for (Entity candidate : environment) {
            float score = this.matcher.match(us, candidate);
            if (score >= highestScore && score >= minScore && score <= maxScore) {
                if (score > highestScore) {
                    winners.clear();
                }
                highestScore = score;
                winners.add(candidate);
            }
        }

        // return one of the best matches (or null if they all suck)
        if (winners.size() == 0) {
            return null;
        }
        return winners.get(rng.nextPositiveInt(winners.size()));
    }

    public PartnerSelectionAttribute(IMatcher matcher, float minScore, float maxScore) {
        this.matcher = matcher;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }
}
