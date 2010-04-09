/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.gui.rulesetmanager.util.DimensionsListener;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.interfaces.ITargetSelection;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author tam
 */
public class AssimilationAction extends AbstractAction {

    private ColorDimensions dimensions;
    private byte ourSharePercent;

    public AssimilationAction(int cost, int mode, ITargetSelection targetSelection, ColorDimensions dimensions, byte ourSharePercent) {
        super("assimilate", cost, mode, targetSelection);
        this.dimensions = dimensions;
        this.ourSharePercent = ourSharePercent;
    }

    public AssimilationAction() {
        super("assimilate");
        dimensions = new ColorDimensions(true, true, true);
    }
    
    public ColorDimensions getDimensionsToMix() {
        return dimensions;
    }

    public void setDimensionsToMix(ColorDimensions dimensionsToMix) {
        this.dimensions = dimensionsToMix;
    }

    public byte getOurSharePercent() {
        return ourSharePercent;
    }

    public void setOurSharePercent(byte ourSharePercent) {
        this.ourSharePercent = ourSharePercent;
    }

    public void executeCallback(Pixel origin, RelativeCoordinate direction, World world) {
        Pixel target = world.get(origin.getLocation(), direction);
        assert (target != null);
        target.getPixelColor().mixWith(origin.getPixelColor(),
                ((float)ourSharePercent) / 100, dimensions);
    }

    protected Map<String, String>parametersCallbackString(Map<String, String> parametersMap) {
        parametersMap.put("dimensions", dimensions.toString());
        parametersMap.put("our share in %", Integer.toString(ourSharePercent));
        return parametersMap;
    }

    protected Map<String, String>parametersCallbackHTML(Map<String, String> parametersMap) {
        parametersMap.put("dimensions", dimensions.toHTML());
        parametersMap.put("our share in %", Integer.toString(ourSharePercent));
        return parametersMap;
    }

    public LinkedHashMap<String,JComponent> parametersCallbackGUI(LinkedHashMap<String, JComponent> parametersMap) {
        JPanel dimensionsPanel = new JPanel();
        JToggleButton btnH = new JToggleButton("H");
        JToggleButton btnS = new JToggleButton("S");
        JToggleButton btnB = new JToggleButton("B");
        DimensionsListener dimensionsListener = new DimensionsListener(dimensions, btnH, btnS, btnB);
        btnH.addActionListener(dimensionsListener);
        btnS.addActionListener(dimensionsListener);
        btnB.addActionListener(dimensionsListener);
        if (dimensions.hue) {
            btnH.setSelected(true);
        }
        if (dimensions.saturation) {
            btnS.setSelected(true);
        }
        if (dimensions.brightness) {
            btnB.setSelected(true);
        }
        dimensionsPanel.add(btnH);
        dimensionsPanel.add(btnS);
        dimensionsPanel.add(btnB);
        parametersMap.put("Dimensions", dimensionsPanel);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(ourSharePercent, 0, 100, 1);
        JSpinner rewardValueSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        rewardValueSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setOurSharePercent(((Integer) ((JSpinner) e.getSource()).getValue()).byteValue());
            }
        });
        parametersMap.put("Our share in %", rewardValueSpinner);

        return parametersMap;
    }
}
