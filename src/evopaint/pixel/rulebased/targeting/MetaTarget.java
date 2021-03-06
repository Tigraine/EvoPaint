/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 * 
 *  This file is part of EvoPaint.
 * 
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.pixel.rulebased.targeting;

import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class MetaTarget extends Target {
    protected List<RelativeCoordinate> directions;

    public MetaTarget(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

    public MetaTarget() {
        this.directions = new ArrayList<RelativeCoordinate>();
    }

    public MetaTarget(MetaTarget target) {
        this.directions = new ArrayList(target.directions);
    }

    public MetaTarget(int numDirections, IRandomNumberGenerator rng) {
        this.directions = new ArrayList<RelativeCoordinate>() {{
                add(RelativeCoordinate.CENTER);
                add(RelativeCoordinate.NORTH);
                add(RelativeCoordinate.NORTH_EAST);
                add(RelativeCoordinate.EAST);
                add(RelativeCoordinate.SOUTH_EAST);
                add(RelativeCoordinate.SOUTH);
                add(RelativeCoordinate.SOUTH_WEST);
                add(RelativeCoordinate.WEST);
                add(RelativeCoordinate.NORTH_WEST);
            }};
        Collections.shuffle(directions, rng.getRandom());
        for (int i = 0; i < 1 - numDirections; i++) {
            directions.remove(directions.size() - 1);
        }
    }

    public int getType() {
        return Target.META_TARGET;
    }

    public int countGenes() {
        return directions.size() + 1; // the 1 is for adding a direction
    }

    public void mutate(int mutatedGene, IRandomNumberGenerator rng) {
        if (mutatedGene < directions.size()) {
            ArrayList<RelativeCoordinate> unusedDirections = new ArrayList<RelativeCoordinate>() {{
                add(RelativeCoordinate.CENTER);
                add(RelativeCoordinate.NORTH);
                add(RelativeCoordinate.NORTH_EAST);
                add(RelativeCoordinate.EAST);
                add(RelativeCoordinate.SOUTH_EAST);
                add(RelativeCoordinate.SOUTH);
                add(RelativeCoordinate.SOUTH_WEST);
                add(RelativeCoordinate.WEST);
                add(RelativeCoordinate.NORTH_WEST);
            }};
            unusedDirections.removeAll(directions);

            // if we used up all directions, we remove the mutated one
            if (unusedDirections.size() == 0) {
                directions.remove(mutatedGene);
            } else {
                // else we give removal and each unused direction an equal chance
                // to replace this one.
                int chosenUnused = rng.nextPositiveInt(unusedDirections.size() + 1);
                if (chosenUnused == unusedDirections.size()) { // the "removal" one
                    directions.remove(mutatedGene);
                } else { // a real direction
                    directions.set(mutatedGene, unusedDirections.get(chosenUnused));
                }
            }
            return;
        }
        mutatedGene -= directions.size();

        if (mutatedGene == 0) {
            if (directions.size() < 9) {
                ArrayList<RelativeCoordinate> unusedDirections =
                        new ArrayList<RelativeCoordinate>() {{
                        add(RelativeCoordinate.CENTER);
                        add(RelativeCoordinate.NORTH);
                        add(RelativeCoordinate.NORTH_EAST);
                        add(RelativeCoordinate.EAST);
                        add(RelativeCoordinate.SOUTH_EAST);
                        add(RelativeCoordinate.SOUTH);
                        add(RelativeCoordinate.SOUTH_WEST);
                        add(RelativeCoordinate.WEST);
                        add(RelativeCoordinate.NORTH_WEST);
                }};
                unusedDirections.removeAll(directions);

                if (unusedDirections.size() == 0) {
                    return;
                }
                
                directions.add(unusedDirections.get(
                        rng.nextPositiveInt(unusedDirections.size())));
            }
            return;
        }

        assert false; // we have an error in our mutatedGene calculation
    }

    public void mixWith(Target theirTarget, float theirShare, IRandomNumberGenerator rng) {
        
        MetaTarget theirMetaTarget = (MetaTarget)theirTarget;

        // cache size() calls for maximum performance
        int ourSize = directions.size();
        int theirSize = theirMetaTarget.directions.size();

        // now mix as many directions as we have in common and add the rest depending
        // on share percentage
        // we have more directions
        if (ourSize > theirSize) {
            int i = 0;
            while (i < theirSize) {
                if (rng.nextFloat() < theirShare) {
                    directions.set(i, theirMetaTarget.directions.get(i));
                }
                i++;
            }
            int removed = 0;
            while (i < ourSize - removed) {
                if (rng.nextFloat() < theirShare) {
                    directions.remove(i);
                    removed ++;
                } else {
                    i++;
                }
            }
        } else { // they have more directions or we have an equal number of directions
           int i = 0;
            while (i < ourSize) {
                if (rng.nextFloat() < theirShare) {
                    directions.set(i, theirMetaTarget.directions.get(i));
                }
                i++;
            }
            while (i < theirSize) {
                if (rng.nextFloat() < theirShare) {
                    directions.add(theirMetaTarget.directions.get(i));
                }
                i++;
            }
        }
    }

    public List<RelativeCoordinate> getDirections() {
        return directions;
    }

    public void setDirections(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

    @Override
    public String toString() {
        if (directions.size() == 0) {
            return "<no targets>";
        }
        if (directions.size() == 8 && false == directions.contains(RelativeCoordinate.CENTER)) {
            return "my neighbors";
        }
        if (directions.size() == 9) {
            return "us";
        }
        String ret = new String();
        ret += "[";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        return ret;
    }

    public String toHTML() {
        if (directions.size() == 0) {
            return "&lt;no targets&gt;";
        }
        if (directions.size() == 8 && false == directions.contains(RelativeCoordinate.CENTER)) {
            return "my neighbors";
        }
        if (directions.size() == 9) {
            return "us";
        }
        String ret = new String();
        ret += "[";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        return ret;
    }
    
}
