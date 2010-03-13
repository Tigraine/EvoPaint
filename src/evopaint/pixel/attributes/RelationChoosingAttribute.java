/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.attributes;

import evopaint.PixelRelation;
import evopaint.entities.Pixel;
import evopaint.entities.World;
import evopaint.interfaces.IAttribute;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.Logger;

import java.util.IdentityHashMap;

/**
 *
 * @author tam
 */
public class RelationChoosingAttribute implements IAttribute {
    private IdentityHashMap<Class, Short> biases;
    private PixelRelation relation;

    public PixelRelation getRelation() {
        return relation;
    }

    public void setRelation(PixelRelation relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "TODO: implement me in RelationChoosingAttribute.java";
    }

    public Class getFavoriteRelationType(IRandomNumberGenerator randomNumberGenerator) {
        
        // sum up all biases to get all possible events
        long sum = 0;
        for (short bias : this.biases.values()) {
            sum += bias;
        }

        // draw from those events using a random number
        double rnd = randomNumberGenerator.nextDouble();
        double lastProbability = 0;
        for (Class relationType : this.biases.keySet()) {
            double probability = lastProbability + ((double)this.biases.get(relationType)) / sum;
            if (lastProbability < rnd && rnd < probability ) {
                return relationType;
            }
            lastProbability = probability;
        }

        if (lastProbability != 1.0) {
            System.err.println("it happened! please send a mail to tam@edu.uni-klu.ac.at (" + lastProbability + ")");
            System.exit(1);
        }

        System.err.println("ERROR: No Probability found, this should not happen (RelationChoosingAttribute.java)");
        System.exit(1);
        return null;
    }

    public void learn(Class relationType) {
        this.biases.put(relationType, (short)(Short.MAX_VALUE / 2));
    }

    public void forget(Class relationType) {
        this.biases.remove(relationType);
    }

    public void promote(Class relationType) {
        Short biasO = this.biases.get(relationType);

        // lil check if we know this relation type at all
        if (biasO == null) {
            Logger.log.warning("WARNING: RelationChoosingAttribute: Unable to promote " + relationType.getName() + ", because it is not known.");
            return;
        }

        short bias = biasO.shortValue();

        // if we are maxed out, demote the rest
        if (bias == Short.MAX_VALUE) {
            for (Class otherRelationType : this.biases.keySet()) {
                if (otherRelationType != relationType) {
                    this.demote(otherRelationType);
                }
            }
            return;
        }

        // else promote this relation
        this.biases.put(relationType, (short)((this.biases.get(relationType) + 1) % Short.MAX_VALUE));
    }

    public void demote(Class relationType) {
        Short biasO = this.biases.get(relationType);
        if (biasO == null) {
            Logger.log.warning("WARNING: RelationChoosingAttribute: Unable to demote " + relationType.getName() + ", because it is not known.");
            return;
        }
        short bias = biasO.shortValue();
        this.biases.put(relationType, (short)Math.max(bias - 1, 1));
    }

    public void resetRelation(World world, IRandomNumberGenerator rng) {
        Class chosenRelationType = getFavoriteRelationType(rng);

        if (relation != null && relation.getClass() == chosenRelationType) {
            relation.resetB(world, rng);
            return;
        }

        try {
            PixelRelation newRelation = (PixelRelation)chosenRelationType.newInstance();
            newRelation.setA(relation.getA());
            newRelation.resetB(world, rng);
            relation = newRelation;
        } catch (InstantiationException e) {
            e.printStackTrace();
            java.lang.System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            java.lang.System.exit(1);
        }
    }

    public RelationChoosingAttribute() {
        this.biases = new IdentityHashMap<Class, Short>();
    }
}
