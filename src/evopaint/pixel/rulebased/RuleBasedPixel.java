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

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.util.mapping.AbsoluteCoordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class RuleBasedPixel extends Pixel {
    private List<Rule> rules;

    public RuleSet createRuleSet() {
        return new RuleSet(rules);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void act(Configuration configuration) {
        if (rules == null) {
            return;
        }
        for (Rule rule : rules) {
            if (rule.apply(this, configuration)) {
                break;
            }
        }
    }

    public void mutate(Configuration config) {
        int numGenes = countGenes();
        int mutatedGene = config.rng.nextPositiveInt(numGenes);
        mutate(mutatedGene, config);
    }

    @Override
    public int countGenes() {
        int ret = super.countGenes();
        for (Rule rule : rules) {
            ret += rule.countGenes();
        }
        ret += 1; // this gene causes the removal of a rule;
        ret += 1; // this gene causes the addition of a rule;
        return ret;
    }

    protected void mutate(int mutatedGene, Configuration config) {
        int numGenesSuper = super.countGenes();
        if (mutatedGene < numGenesSuper) {
            super.mutate(mutatedGene, config.rng);
            return;
        }
        mutatedGene -= numGenesSuper;

        for (int i = 0; i < rules.size(); i++) {
            int numGenesRule = rules.get(i).countGenes();
            if (mutatedGene < numGenesRule) {
                Rule newRule = new Rule(rules.get(i));
                newRule.mutate(mutatedGene, config.rng);
                rules.set(i, newRule);
                return;
            }
            mutatedGene -= numGenesRule;
        }

        if (mutatedGene == 0) {
            if (rules.size() == 0) {
                return;
            }
            rules.remove(config.rng.nextPositiveInt(rules.size()));
            return;
        }
        mutatedGene -= 1;

        if (mutatedGene == 0) {
            rules.add(new Rule(config.usedActions, config.rng));
            return;
        }
        mutatedGene -= 1;

        assert false;
    }

    public void mixWith(RuleBasedPixel them, float theirShare, IRandomNumberGenerator rng) {
        super.mixWith(them, theirShare, rng);
     
        // mix rules
        List<Rule> theirRules = them.getRules();

        // cache size() calls for maximum performance
        int ourSize = rules.size();
        int theirSize = theirRules.size();

        // now mix as many rules as we have in common and add the rest depending
        // on share percentage
        // we have more rules
        if (ourSize > theirSize) {
            int i = 0;
            while (i < theirSize) {
                Rule newRule = new Rule(rules.get(i));
                newRule.mixWith(theirRules.get(i), theirShare, rng);
                rules.set(i, newRule);
                i++;
            }
            int removed = 0;
            while (i < ourSize - removed) {
                if (rng.nextFloat() < theirShare) {
                    rules.remove(i);
                    removed ++;
                } else {
                    i++;
                }
            }
        } else { // they have more rules or we have an equal number of rules
           int i = 0;
            while (i < ourSize) {
                Rule newRule = new Rule(rules.get(i));
                newRule.mixWith(theirRules.get(i), theirShare, rng);
                rules.set(i, newRule);
                i++;
            }
            while (i < theirSize) {
                if (rng.nextFloat() < theirShare) {
                    rules.add(theirRules.get(i));
                }
                i++;
            }
        }
    }

    public RuleBasedPixel(RuleBasedPixel pixel) {
        super(pixel);
        this.rules = new ArrayList(pixel.rules);
    }

    public RuleBasedPixel(RuleBasedPixel pixel, List<Rule> useTheseRules) {
        super(pixel);
        this.rules = new ArrayList(useTheseRules);
    }

    public RuleBasedPixel(PixelColor pixelColor, AbsoluteCoordinate location, int energy, List<Rule> rules) {
        super(pixelColor, location, energy);
        this.rules = new ArrayList(rules);
    }
}
