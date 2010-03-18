/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.actions;

import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.interfaces.IAction;

/**
 *
 * @author tam
 */
public abstract class AbstractAction implements IAction {

    protected int cost;
    protected String name;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract int execute(Pixel pixel, World world);

    public AbstractAction(int cost) {
        this.cost = cost;
        this.name = "unnamed action";
    }

    public AbstractAction(int cost, String name) {
        this.cost = cost;
        this.name = name;
    }
}
