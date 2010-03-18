/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.World;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author tam
 */
public abstract class AbstractAgent {
    
    private int energy;
    protected Collection<Rule> rules;
    private Object memory;

    public void learn(Rule rule) {
        rules.add(rule);
    }

    public void forget(Rule rule) {
        rules.remove(rule);
    }

    public int getEnergy() {
        return energy;
    }
    
    public boolean isAlive() {
        return energy > 0;
    }

    public void save(Object o) {
        memory = o;
    }

    public Object restore() {
        return memory;
    }

    public void purgeMemory() {
        memory = null;
    }

    public void reward(int energy) {
        this.energy += energy;
    }
    
    public abstract void act(World world);
        
    public AbstractAgent(int energy) {
        this.energy = energy;
        this.rules = new ArrayList<Rule>();
    }
}
