/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.pixel.rulebased.targeting.IHaveTarget;
import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.ICopyable;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.ActionTarget;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.util.ExceptionHandler;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author tam
 */
public abstract class Action implements IHaveTarget, ICopyable {

    protected int energyChange;
    private IActionTarget target;

    protected Action(int energyChange, ActionMetaTarget target) {
        this.energyChange = energyChange;
        this.target = target;
    }

    protected Action() {
        this.target = new ActionTarget();
    }

    public int getEnergyChange() {
        return energyChange;
    }

    public void setEnergyChange(int energyChange) {
        this.energyChange = energyChange;
    }

    public IActionTarget getTarget() {
        return target;
    }

    public void setTarget(ITarget target) {
        this.target = (IActionTarget)target;
    }

    public Action getCopy() {
        Action newAction = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newAction = (Action) in.readObject();
        } catch (ClassNotFoundException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
        return newAction;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += getName();

        ret += " (";/*
        if (false == this instanceof IdleAction) {
            if (target instanceof MetaTarget) {
                ret += "Targets: ";
            } else {
                ret += "Target: ";
            }
            ret += target.toString();
            ret += ", ";
        }
       */
        Map<String, String> parametersMap = addParametersString(new LinkedHashMap<String, String>());
        for (String parameterName : parametersMap.keySet()) {
            ret += parameterName + ": " + parametersMap.get(parameterName) + ", ";
        }

        ret = ret.substring(0, ret.length() - 2);
        ret += ")";

        return ret;
    }

    public String toHTML() {
        String ret = new String();
        ret += "<b>" + getName() + "</b>";
        ret += " (";
        /*
        if (false == this instanceof IdleAction) {
            ret += "<span style='color: #777777;'>";
            if (target instanceof MetaTarget) {
                ret += "Targets";
            } else {
                ret += "Target";
            }
            ret += ":</span> ";
            ret += target.toHTML();
            ret += ", ";
        }
*/
        Map<String, String> parametersMap = addParametersHTML(new LinkedHashMap<String, String>());
        for (String parameterName : parametersMap.keySet()) {
            ret += "<span style='color: #777777;'>" + parameterName + ":</span> " +
                    parametersMap.get(parameterName) + ", ";
        }
        
        ret = ret.substring(0, ret.length() - 2);
        ret += ")";

        return ret;
    }

    public int execute(Pixel actor, Configuration configuration) {

        return target.execute(this, actor, configuration);
    }
    
    public LinkedHashMap<String, JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(energyChange, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        JSpinner energyChangeSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        //energyChangeSpinner.setEditor(new JSpinner.NumberEditor(energyChangeSpinner, "+#;-#"));
        energyChangeSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                energyChange = (Integer)((JSpinner)e.getSource()).getValue();
            }
        });
        parametersMap.put("My Energy Change", energyChangeSpinner);

        return parametersMap;
    }

    public Map<String, String> addParametersHTML(Map<String, String> parametersMap) {
        parametersMap.put("energy change", Integer.toString(energyChange));
        return parametersMap;
    }

    public Map<String, String> addParametersString(Map<String, String> parametersMap) {
        parametersMap.put("energy change", Integer.toString(energyChange));
        return parametersMap;
    }

    public abstract int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration);

}
