/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.attributes;

import evopaint.entities.Pixel;
import evopaint.entities.World;
import evopaint.interfaces.IAttribute;
import evopaint.interfaces.IMatcher;
import evopaint.interfaces.IRandomNumberGenerator;

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

    public Pixel findPartner(World world, Pixel us, int radius, IRandomNumberGenerator rng) {

        // get the possible matches from the environment of our pixel
        List<Pixel> environment = new ArrayList<Pixel>((2*radius+1)*(2*radius+1)-1);
        SpacialAttribute sa = us.getSpacialAttribute();
        for (int y = sa.getY() - radius; y <= sa.getY() + radius; y++) {
            for (int x = sa.getX() - radius; x <= sa.getX() + radius; x++) {

                // skip self
                if (x == sa.getX() && y == sa.getY()) {
                    continue;
                }

                environment.add(world.getPixels().get(x, y));
            }
        }

        // scan the environment for good matches
        List<Pixel> winners = new ArrayList<Pixel>();
        float highestScore = 0.0f;
        for (Pixel candidate : environment) {
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
