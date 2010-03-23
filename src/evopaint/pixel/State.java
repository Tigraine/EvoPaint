/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel;

/**
 *
 * @author tam
 */
public class State {
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public State(String name) {
        this.name = name;
    }

    public State(State state) {
        this.name = state.name;
    }
}
