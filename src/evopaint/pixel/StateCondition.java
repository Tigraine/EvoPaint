/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

import evopaint.World;
import evopaint.pixel.misc.ObjectComparisonOperator;
import evopaint.pixel.interfaces.ICondition;

/**
 *
 * @author tam
 *//*
public class StateCondition implements ICondition {

    private State state;
    private ObjectComparisonOperator comparisonOperator;

    @Override
    public String toString() {
        return "state " + comparisonOperator.toString() + " " + state.toString();
    }

    @Override
    public boolean isMet(Pixel pixel, World world) { // world is just for interface compability, but who knows, we might just need it one day ;)
        return comparisonOperator.compare(pixel.getState(), state);
    }

    public StateCondition(State state, ObjectComparisonOperator comparisonOperator) {
        this.state = state;
        this.comparisonOperator = comparisonOperator;
    }
 
 
}
 */