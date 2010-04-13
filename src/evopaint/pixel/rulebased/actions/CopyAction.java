/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.gui.rulesetmanager.JTargetButton;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public class CopyAction extends Action {

    public CopyAction(int energyChange, ActionMetaTarget target) {
        super(energyChange, target);
    }

    public CopyAction() {
    }

    public String getName() {
        return "copy";
    }

    public int execute(Pixel actor, RelativeCoordinate direction, Configuration configuration) {
        Pixel target = configuration.world.get(actor.getLocation(), direction);
        if (target != null) {
            return 0;
        }
        Pixel newPixel = new RuleBasedPixel(
                new PixelColor(actor.getPixelColor()),
                new AbsoluteCoordinate(actor.getLocation(), direction, configuration.world),
                actor.getEnergy() + getEnergyChange(),
                new RuleSet(((RuleBasedPixel)actor).getRuleSet()));
        configuration.world.set(newPixel);

        return energyChange;
    }

    @Override
    public LinkedHashMap<String,JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        JTargetButton jTargetButton = new JTargetButton(this);
        parametersMap.put("Target", jTargetButton);

        return parametersMap;
    }
    
}
