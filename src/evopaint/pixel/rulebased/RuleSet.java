/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.interfaces.IDescribable;
import evopaint.pixel.rulebased.interfaces.INameable;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tam
 */
public class RuleSet implements Serializable, INameable, IDescribable, ICopyable {
    private String name;
    private String description;
    private List<IRule> rules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String ret = "Rule set '" + name + "' consists of:\n";
        for (IRule rule : rules) {
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

    public List<IRule> getRules() {
        return rules;
    }

    public void setRules(List<IRule> rules) {
        this.rules = rules;
    }

    public RuleSet getCopy() {
        RuleSet newRuleSet = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newRuleSet = (RuleSet) in.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return newRuleSet;
    }

    public RuleSet(String name, String description, List<IRule> rules) {
        this.name = name;
        this.description = description;
        this.rules = rules;
    }

    public RuleSet(RuleSet ruleSet) {
        this.name = ruleSet.name;
        this.description = ruleSet.description;
        this.rules = ruleSet.rules;
    }
}
