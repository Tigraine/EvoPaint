package evopaint.commands;

import evopaint.gui.Showcase;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 25.03.2010
 * Time: 23:44:02
 * To change this template use File | Settings | File Templates.
 */
public class FillSelectionCommandScattered extends FillSelectionCommand {
    private final int DENSITY_HALF = 2;
    public FillSelectionCommandScattered(Showcase showcase) {
        super(showcase);
        super.density = DENSITY_HALF;
    }
}
