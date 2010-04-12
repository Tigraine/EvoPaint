/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased;

import evopaint.Configuration;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.pixel.rulebased.interfaces.IParameterized;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedActionTarget;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.Serializable;
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
public abstract class Action implements IParameterized, INamed, IHTML, Serializable {

    protected int energyChange;
    private IActionTarget target;

    protected Action(int energyChange, IActionTarget target) {
        this.energyChange = energyChange;
        this.target = target;
    }

    protected Action() {
        target = new SpecifiedActionTarget();
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

    public void setTarget(IActionTarget target) {
        this.target = target;
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += getName();

        ret += " (";
       
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

        Map<String, String> parametersMap = addParametersString(new LinkedHashMap<String, String>());
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
        energyChangeSpinner.setEditor(new JSpinner.NumberEditor(energyChangeSpinner, "+#;-#"));
        energyChangeSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                energyChange = (Integer)((JSpinner)e.getSource()).getValue();
            }
        });
        parametersMap.put("Energy Change", energyChangeSpinner);

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
