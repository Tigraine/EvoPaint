/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tam
 */
public class RuleSetCollection implements Serializable {
    private String name;
    private String description;
    private List<RuleSet> rulesets;

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<RuleSet> getRulesets() {
        return rulesets;
    }

    public RuleSetCollection(String name, String description, List<RuleSet> rulesets) {
        this.name = name;
        this.description = description;
        this.rulesets = rulesets;
    }
}
