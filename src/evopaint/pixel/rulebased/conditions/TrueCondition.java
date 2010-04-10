/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.conditions;

import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import java.util.LinkedHashMap;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public class TrueCondition extends Condition {

    public TrueCondition(IConditionTarget target) {
        super(target);
    }

    public TrueCondition() {
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
