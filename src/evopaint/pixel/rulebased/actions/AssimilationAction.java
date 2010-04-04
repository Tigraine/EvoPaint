/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.actions;

import evopaint.Configuration;
import evopaint.pixel.rulebased.AbstractAction;
import evopaint.World;
import evopaint.gui.rulesetmanager.util.DimensionsListener;
import evopaint.gui.util.AutoSelectOnFocusSpinner;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.util.mapping.RelativeCoordinate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    public String getName() {
        return "Assimilate";
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

    @Override
    public String toString() {
        String ret = "assimilate(";
        ret += "targets: " + getDirectionsString();
        ret += ", dimensions: " + dimensions.toHTML();
        ret += ", our share in %: " + ourSharePercent;
        ret += ", cost: " + getCost();
        ret += ")";
        return ret;
    }

    @Override
    public String toHTML() {
        String ret = "<b>assimilate</b>(";
        ret += "<span style='color: #777777;'>targets:</span> " + getDirectionsString();
        ret += ", <span style='color: #777777;'>dimensions:</span> " + dimensions.toHTML();
        ret += ", <span style='color: #777777;'>our share in %:</span> " + ourSharePercent;
        ret += ", <span style='color: #777777;'>cost:</span> " + getCost();
        ret += ")";
        return ret;
    }

    public int execute(Pixel us, World world) {

        for (RelativeCoordinate direction : getDirections()) {
            Pixel them = world.get(us.getLocation(), direction);
            if (them == null) { // never forget to skip empty spots
                continue;
            }
            them.getPixelColor().mixWith(us.getPixelColor(), ((float)ourSharePercent) / 100, dimensions);
        }

        return getCost();
    }

    public LinkedHashMap<String,JComponent> getParametersForGUI(Configuration configuration) {
        LinkedHashMap<String,JComponent> ret = new LinkedHashMap<String,JComponent>();

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
        ret.put("Dimensions", dimensionsPanel);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(ourSharePercent, 0, 100, 1);
        JSpinner rewardValueSpinner = new AutoSelectOnFocusSpinner(spinnerModel);
        rewardValueSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setOurSharePercent(((Integer) ((JSpinner) e.getSource()).getValue()).byteValue());
            }
        });
        ret.put("Our share in %", rewardValueSpinner);

        return ret;
    }

    public AssimilationAction(int reward, List<RelativeCoordinate> directions, ColorDimensions dimensions, Byte ourSharePercent) {
        super(reward, directions);
        this.dimensions = dimensions;
        this.ourSharePercent = ourSharePercent;
    }

    public AssimilationAction() {
        super(0, new ArrayList<RelativeCoordinate>(9));
        this.dimensions = new ColorDimensions(false, false, false);
    }
}
