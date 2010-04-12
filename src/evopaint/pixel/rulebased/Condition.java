/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.pixel.rulebased.interfaces.IParameterized;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedConditionTarget;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author tam
 */
public abstract class Condition implements IParameterized, INamed, IHTML, Serializable {

    private IConditionTarget target;

    public Condition(IConditionTarget target) {
        this.target = target;
    }

    public Condition() {
        this.target = new SpecifiedConditionTarget();
    }

    public IConditionTarget getTarget() {
        return target;
    }

    public void setTarget(IConditionTarget target) {
        this.target = target;
    }

    public Map<String, String>addParametersString(Map<String, String> parametersMap) {
        return parametersMap;
    }

    public Map<String, String>addParametersHTML(Map<String, String> parametersMap) {
        return parametersMap;
    }

    public boolean isMet(Pixel actor, Configuration configuration) {
        return target.meets(this, actor, configuration);
    }
    
    public abstract boolean isMet(Pixel actor, Pixel target);

}
