/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.Pixel;
import java.util.LinkedHashMap;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public class TrueCondition extends Condition {

    public TrueCondition() {
    }

    @Override // always true condition does not need target checking
    public boolean isMet(Pixel actor, Configuration configuration) {
        return true;
    }

    public boolean isMet(Pixel us, Pixel them) {
        return true;
    }

    public String getName() {
        return "true";
    }

    @Override
    public String toString() {
        return "true";
    }

    public String toHTML() {
        return "true";
    }

    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String,JComponent> parametersMap) {
        return parametersMap;
    }
}
