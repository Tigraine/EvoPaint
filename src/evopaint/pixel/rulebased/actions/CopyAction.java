/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author tam
 */
public class CopyAction extends AbstractAction {

    public CopyAction(int cost, List<RelativeCoordinate> directions) {
        super("copy", cost, directions);
    }

    public CopyAction() {
        super("copy");
    }

    public void executeCallback(Pixel origin, RelativeCoordinate direction, World world) {
        Pixel target = world.get(origin.getLocation(), direction);
        if (target != null) { // cannot copy to occupied spots
            return;
        }
        Pixel newPixel = new RuleBasedPixel(
                new PixelColor(origin.getPixelColor()),
                new AbsoluteCoordinate(origin.getLocation(), direction, world),
                origin.getEnergy() - getCost(),
                new RuleSet(((RuleBasedPixel)origin).getRuleSet()));
        world.set(newPixel);
    }

    protected Map<String, String>parametersCallbackString(Map<String, String> parametersMap) {
        return parametersMap;
    }

    protected Map<String, String>parametersCallbackHTML(Map<String, String> parametersMap) {
        return parametersMap;
    }
    
    public LinkedHashMap<String,JComponent> parametersCallbackGUI(LinkedHashMap<String, JComponent> parametersMap) {
        return parametersMap;
    }
}