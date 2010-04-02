/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.interfaces.INamed;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 *
 * @author tam
 */
public class RuleSetCollection implements Serializable, INamed, ICopyable {
    private String name;
    private String description;
    private int numRuleSets;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getNumRuleSets() {
        return numRuleSets;
    }

    public void setNumRuleSets(int numRuleSets) {
        this.numRuleSets = numRuleSets;
    }

    public RuleSetCollection getCopy() {
        RuleSetCollection newRuleSetCollection = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newRuleSetCollection = (RuleSetCollection) in.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return newRuleSetCollection;
    }

    public RuleSetCollection(String name, String description, int numRuleSets) {
        this.name = name;
        this.description = description;
        this.numRuleSets = numRuleSets;
    }

}

