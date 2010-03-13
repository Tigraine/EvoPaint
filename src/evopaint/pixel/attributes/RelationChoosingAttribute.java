/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.attributes;

import evopaint.Config;
import evopaint.RandomNumberGeneratorWrapper;
import evopaint.interfaces.IRandomNumberGenerator;

import java.util.IdentityHashMap;

/**
 *
 * @author tam
 */
public class RelationChoosingAttribute {
    private IdentityHashMap<Class, Short> biases;

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
            System.err.println("it happened! please send a mail to tam@edu.uni-klu.ac.at"+lastProbability);
            System.exit(1);
        }

        System.err.println("ERROR: No Probability found, this should not happen (Strategy.java)");
        System.exit(1);
        return null;
    }

    public void learn(Class relationType) {
        this.biases.put(relationType, (short)1);
    }

    public void forget(Class relationType) {
        this.biases.remove(relationType);
    }

    public void promote(Class relationType) {
        Short biasO = this.biases.get(relationType);

        // lil check if we know this relation type at all
        if (biasO == null) {
            System.err.println("WARNING: RelationCapabilityAttribute: Unable to promote " + relationType.getName() + ", because it is not known.");
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
            System.err.println("WARNING: RelationCapabilityAttribute: Unable to demote " + relationType.getName() + ", because it is not known.");
            return;
        }
        short bias = biasO.shortValue();
        this.biases.put(relationType, (short)Math.max(bias - 1, 0));
    }

}
