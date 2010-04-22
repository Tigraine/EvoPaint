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

import evopaint.pixel.rulebased.interfaces.IDescribable;
import evopaint.pixel.rulebased.interfaces.INameable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class RuleSet implements Serializable, INameable, IDescribable {
    private String name;
    private String description;
    private List<Rule> rules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String ret = "Rule set '" + name + "' consists of:\n";
        for (Rule rule : rules) {
            ret += "\t" + rule.toString() + "\n";
        }
        return ret;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public RuleSet(String name, String description, List<Rule> rules) {
        this.name = name;
        this.description = description;
        this.rules = rules;
    }

    public RuleSet(RuleSet ruleSet) {
        this.name = ruleSet.name;
        this.description = ruleSet.description;
        this.rules = new ArrayList(ruleSet.rules);
    }

    public RuleSet(List<Rule> rules) {
        this.name = "new rule set";
        this.description = "describe me";
        this.rules = rules;
    }
    
}
