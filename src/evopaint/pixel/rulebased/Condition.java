/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.pixel.rulebased.targeting.IHaveTarget;
import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.targeting.ConditionTarget;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.util.ExceptionHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 *
 * @author tam
 */
public abstract class Condition implements IHaveTarget, ICopyable {

    private IConditionTarget target;

    public Condition(IConditionTarget target) {
        this.target = target;
    }

    public Condition() {
        this.target = new ConditionTarget();
    }

    public IConditionTarget getTarget() {
        return target;
    }

    public void setTarget(ITarget target) {
        this.target = (IConditionTarget)target;
    }

    public Condition getCopy() {
        Condition newCondition = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newCondition = (Condition) in.readObject();
        } catch (ClassNotFoundException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
        return newCondition;
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
